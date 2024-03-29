package arroba.fooddelivery.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherDataScheduler {
    private final WeatherDataService weatherDataService;

    public WeatherDataScheduler(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @Scheduled(cron = "0 15 * * * *") // Run every hour, 15 minutes after the hour
    public void scheduleWeatherDataImport() {
        weatherDataService.importWeatherData();
    }
}
