package arroba.fooddelivery.Service;

import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.Repository.WeatherDataRepository;
import arroba.fooddelivery.Mapper.EntityDtoMapper;
import arroba.fooddelivery.DTO.WeatherDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.time.ZoneOffset;
import java.util.List;



@Service
public class WeatherDataService {

    private WeatherDataRepository weatherDataRepository;
    private final EntityDtoMapper entityDtoMapper;
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataService.class);

    public WeatherDataService(WeatherDataRepository weatherDataRepository, EntityDtoMapper entityDtoMapper) {
        this.weatherDataRepository = weatherDataRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Transactional
    public void importWeatherData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php"))
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(response.body());

            String timestampStr = doc.getDocumentElement().getAttribute("timestamp");
            LocalDateTime timestamp = LocalDateTime.ofEpochSecond(Long.parseLong(timestampStr), 0, ZoneOffset.UTC);

            NodeList stations = doc.getElementsByTagName("station");
            for (int i = 0; i < stations.getLength(); i++) {
                Element station = (Element) stations.item(i);
                WeatherData weatherData = parseWeatherData(station, timestamp);
                if (weatherData != null) {
                    weatherDataRepository.save(weatherData);
                    logger.info("Weather data imported for station: {}", weatherData.getStationName());
                }
                // The history of imported weather data
//                if (weatherData != null) {
//                    // Check if an entry already exists for the station
//                    WeatherData existingData = weatherDataRepository.findByStationName(weatherData.getStationName());
//                    if (existingData == null) {
//                        // If no existing entry found, save the new weather data
//                        weatherDataRepository.save(weatherData);
//                        logger.info("New weather data imported for station: {}", weatherData.getStationName());
//                    } else {
//                        logger.info("Weather data for station {} already exists, skipping insertion.", weatherData.getStationName());
//                    }
//                }
            }
        } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
            logger.error("Error importing weather data: {}", e.getMessage());
        }
    }

    private WeatherData parseWeatherData(Element station, LocalDateTime timestamp) {
        try {
            String stationName = getNodeTextContent(station, "name");
            String wmoCode = getNodeTextContent(station, "wmocode");
            double airTemperature = getDoubleNodeTextContent(station, "airtemperature");
            double windSpeed = getDoubleNodeTextContent(station, "windspeed");
            String weatherPhenomenon = getNodeTextContent(station, "phenomenon");

            return new WeatherData(stationName, wmoCode, airTemperature, windSpeed, weatherPhenomenon, timestamp);
        } catch (Exception e) {
            logger.error("Error parsing weather data: {}", e.getMessage());
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


    public List<WeatherDataDTO> getAllWeatherData() {
        List<WeatherData> weatherDataList = weatherDataRepository.findAll();
        return entityDtoMapper.mapWeatherEntitiesToDTOs(weatherDataList);
    }

    public void saveWeatherData(WeatherData weatherData) {
        weatherDataRepository.save(weatherData);
    }
}


