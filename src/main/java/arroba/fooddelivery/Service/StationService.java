package arroba.fooddelivery.Service;

import arroba.fooddelivery.DTO.StationDTO;

import arroba.fooddelivery.Entity.Station;
import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.Mapper.EntityDtoMapper;
import arroba.fooddelivery.Repository.StationRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@Service
public class StationService {
    private final StationRepository stationRepository;
    private final EntityDtoMapper entityDtoMapper;
    private static final Logger logger = LoggerFactory.getLogger(StationService.class);

    public StationService(StationRepository stationRepository, EntityDtoMapper entityDtoMapper) {
        this.stationRepository = stationRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Transactional
    public void importStationData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php"))
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(response.body());

            NodeList stations = doc.getElementsByTagName("station");
            for (int i = 0; i < stations.getLength(); i++) {
                Element stationElement = (Element) stations.item(i);
                Station station = parseStation(stationElement);
                if (station != null) {
                    stationRepository.save(station);
                    logger.info("Station data imported: {}", station.getName());
                }
            }
        } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
            logger.error("Error importing station data: {}", e.getMessage());
        }
    }
    private Station parseStation(Element stationElement) {
        try {
            String name = getNodeTextContent(stationElement, "name");
            double longitude = getDoubleNodeTextContent(stationElement, "longitude");
            double latitude = getDoubleNodeTextContent(stationElement, "latitude");

            return new Station(name, longitude, latitude);
        } catch (Exception e) {
            logger.error("Error parsing station data: {}", e.getMessage());
            return null;
        }
    }

    private String getNodeTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    private double getDoubleNodeTextContent(Element element, String tagName) {
        String textContent = getNodeTextContent(element, tagName);
        if (textContent != null && !textContent.isEmpty()) {
            return Double.parseDouble(textContent);
        }
        return 0.0;
    }
    public List<StationDTO> getAllStationData() {
        List<Station> stationList = stationRepository.findAll();
        return entityDtoMapper.mapStationEntitiesToDTOs(stationList);
    }
}
