package arroba.fooddelivery.Service;

import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.Repository.WeatherDataRepository;
import arroba.fooddelivery.Mapper.EntityDtoMapper;
import arroba.fooddelivery.DTO.WeatherDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                    // Save the new weather data regardless of existing entries
                    weatherDataRepository.save(weatherData);
                    logger.info("New weather data imported for station: {}", weatherData.getStationName());
                }
            }
        } catch (IOException | InterruptedException | ParserConfigurationException | SAXException e) {
            logger.error("Error importing weather data: {}", e.getMessage());
        }
    }

/* testing purpose: updating weather data entries in db
    if (weatherData != null) {
        // Update the weather data for the station if it already exists
        WeatherData existingData = weatherDataRepository.findByStationName(weatherData.getStationName());
        if (existingData != null) {
            existingData.setAirTemperature(weatherData.getAirTemperature());
            existingData.setWeatherPhenomenon(weatherData.getWeatherPhenomenon());
            existingData.setWindSpeed(weatherData.getWindSpeed());
            existingData.setWmoCode(weatherData.getWmoCode());
            weatherDataRepository.save(existingData);
            logger.info("Weather data updated for station: {}", weatherData.getStationName());
 */

    private WeatherData parseWeatherData(Element station, LocalDateTime timestamp) {
        try {
            String stationName = getNodeTextContent(station, "name");
            String city = extractCityFromStationName(stationName);
            String wmoCode = getNodeTextContent(station, "wmocode");
            double airTemperature = getDoubleNodeTextContent(station, "airtemperature");
            double windSpeed = getDoubleNodeTextContent(station, "windspeed");
            String weatherPhenomenon = getNodeTextContent(station, "phenomenon");

            return new WeatherData(stationName,city, wmoCode, airTemperature, windSpeed, weatherPhenomenon, timestamp);
        } catch (Exception e) {
            logger.error("Error parsing weather data: {}", e.getMessage());
            return null;
        }
    }
    private String extractCityFromStationName(String stationName) {
        // Split the station name, take the first part as the city name
        String[] parts = stationName.split("-");
        return parts.length > 0 ? parts[0] : ""; // Return the first part (city name), or an empty string if not found
    }
    public WeatherData fetchLatestWeatherData(String city) {
        // Log the city for which weather data is being fetched
        logger.info("Fetching latest weather data for city: {}", city);

        // Assuming the WeatherDataRepository has a method to find the latest weather data by city
        WeatherData latestWeatherData = weatherDataRepository.findTopByCityOrderByTimestampDesc(city);

        // Log the result of the query
        if (latestWeatherData != null) {
            logger.info("Latest weather data fetched successfully for city: {}", city);
        } else {
            logger.warn("Failed to fetch weather data for city: {}", city);
        }
        // Return the fetched weather data
        return latestWeatherData;
    }

    private String getNodeTextContent(Element element, String tagName) {
        //  Retrieves the text content of an XML element with a specified tag name (String)
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    private double getDoubleNodeTextContent(Element element, String tagName) {
        //  Retrieves the text content of an XML element with a specified tag name (Double)
        String textContent = getNodeTextContent(element, tagName);
        if (textContent != null && !textContent.isEmpty()) {
            return Double.parseDouble(textContent);
        }
        return 0.0;
    }

//    Retrieves all weather data from the repository and maps them to DTO
    public List<WeatherDataDTO> getAllWeatherData() {
        List<WeatherData> weatherDataList = weatherDataRepository.findAll();
        return entityDtoMapper.mapWeatherEntitiesToDTOs(weatherDataList);
    }

}


