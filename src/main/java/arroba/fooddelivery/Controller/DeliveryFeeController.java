package arroba.fooddelivery.Controller;

import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.Service.CalculationService;
import arroba.fooddelivery.Service.InputValidatorService;
import arroba.fooddelivery.Service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import arroba.fooddelivery.Service.WeatherDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveryfee")
public class DeliveryFeeController {

    private final InputValidatorService validatorService;
    private final CalculationService calculationService;
    private final WeatherDataService weatherDataService;
    private static final Logger logger = LoggerFactory.getLogger(StationService.class);

    public DeliveryFeeController(InputValidatorService validatorService, CalculationService calculationService, WeatherDataService weatherDataService) {
        this.validatorService = validatorService;
        this.calculationService = calculationService;
        this.weatherDataService = weatherDataService;
    }
    // Calculates the delivery fee based on city and vehicle type
    @GetMapping("/example")
    public ResponseEntity<?> calculateDeliveryFee(@RequestParam String city, @RequestParam String vehicleType) {
        // Check if the provided city is valid
        if (!validatorService.isValidCity(city)) {
            return ResponseEntity.badRequest().body("Invalid city: " + city);
        }
        // Check if the provided vehicle type is valid
        if (!validatorService.isValidVehicleType(vehicleType)) {
            return ResponseEntity.badRequest().body("Invalid vehicle type: " + vehicleType);
        }
        // Fetch the latest weather data for the specified city
        WeatherData weatherData = weatherDataService.fetchLatestWeatherData(city);
        if (weatherData == null) {
            logger.warn("Failed to fetch weather data for city: {}", city);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch weather data.");
        }
        // Calculate the total delivery fee, base fee, and extra fee
        double totalFee = calculationService.calculateDeliveryFee(city, vehicleType, weatherData);
        double baseFee = calculationService.calculateRBF(city, vehicleType);
        double extraFee = calculationService.calculateEFFW(weatherData, vehicleType);

        // Build a response string containing the breakdown of the delivery fee calculation
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Latest weather data for ").append(city).append(": (").append(weatherData.getStationName()).append(")\n");
        responseBuilder.append("• Air temperature = ").append(weatherData.getAirTemperature()).append(" °C -> ATEF = ").append(extraFee).append(" €\n");
        responseBuilder.append("• Wind speed = ").append(weatherData.getWindSpeed()).append(" m/s -> WSEF = ").append(extraFee).append(" €\n");
        responseBuilder.append("• Weather phenomenon = ").append(weatherData.getWeatherPhenomenon()).append(" -> WPEF = ").append(extraFee).append(" €\n");
        responseBuilder.append("Total delivery fee = RBF + ATEF + WSEF + WPEF = ").append(baseFee).append(" + ").append(extraFee).append(" + ").append(extraFee).append(" + ").append(extraFee).append(" = ").append(totalFee).append(" €\n");


        return ResponseEntity.ok(responseBuilder.toString());
    }

    //Tests fetching weather data for a specified city -- testing purpose
    @GetMapping("/fetch")
    public ResponseEntity<?> testFetchWeatherData(@RequestParam String city) {
        WeatherData weatherData = weatherDataService.fetchLatestWeatherData(city);
        if (weatherData != null) {
            return ResponseEntity.ok(weatherData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch weather data for city: " + city);
        }
    }

}