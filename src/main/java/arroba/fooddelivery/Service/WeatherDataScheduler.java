package arroba.fooddelivery.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WeatherDataScheduler {
    private final WeatherDataService weatherDataService;

    public WeatherDataScheduler(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }


    //  @Scheduled(cron = "*/30 * * * * *") -- testing purpose
    @Scheduled(cron = "0 15 * * * *") // Run every hour, 15 minutes after the hour
    public void scheduleWeatherDataImport() {
            weatherDataService.importWeatherData();
        }
}
