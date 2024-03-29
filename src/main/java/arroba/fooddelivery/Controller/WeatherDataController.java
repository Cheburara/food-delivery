package arroba.fooddelivery.Controller;

import arroba.fooddelivery.DTO.WeatherDataDTO;
import arroba.fooddelivery.Service.WeatherDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherDataController {

    private final WeatherDataService weatherDataService;

    public WeatherDataController(WeatherDataService weatherDataService) {
        this.weatherDataService = weatherDataService;
    }

    @GetMapping("/import")
    public ResponseEntity<String> importWeatherData() {
        try {
            weatherDataService.importWeatherData();
            return ResponseEntity.ok("Weather data imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import weather data: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<WeatherDataDTO>> getAllWeatherData() {
        List<WeatherDataDTO> weatherDataList = weatherDataService.getAllWeatherData();
        return ResponseEntity.ok(weatherDataList);
    }
}