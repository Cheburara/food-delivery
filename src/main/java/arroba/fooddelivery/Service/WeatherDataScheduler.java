package arroba.fooddelivery.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherDataScheduler {

    private final String weatherDataUrl = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    @Scheduled(cron = "0 15 * * * *") // Run every hour, 15 minutes after the hour
    public void importWeatherData() {

        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(weatherDataUrl, String.class);
        
    }

}
