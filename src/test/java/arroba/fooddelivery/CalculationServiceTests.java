package arroba.fooddelivery;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import arroba.fooddelivery.Service.CalculationService;
import arroba.fooddelivery.Entity.WeatherData;

import java.time.LocalDateTime;

public class CalculationServiceTests {

    @Test
    public void test1() {
        //testCalculateRBF
        CalculationService calculationService = new CalculationService();

        // Test RBF calculation for different cities and vehicle types
        assertEquals(4.0, calculationService.calculateRBF("Tallinn", "Car"));
        assertEquals(3.5, calculationService.calculateRBF("Tallinn", "Scooter"));
        assertEquals(3.0, calculationService.calculateRBF("Tallinn", "Bike"));

        assertEquals(3.5, calculationService.calculateRBF("Tartu", "Car"));
        assertEquals(3.0, calculationService.calculateRBF("Tartu", "Scooter"));
        assertEquals(2.5, calculationService.calculateRBF("Tartu", "Bike"));

        assertEquals(3.0, calculationService.calculateRBF("Pärnu", "Car"));
        assertEquals(2.5, calculationService.calculateRBF("Pärnu", "Scooter"));
        assertEquals(2.0, calculationService.calculateRBF("Pärnu", "Bike"));

        // Test RBF calculation for unknown city and vehicle type
        assertEquals(0.0, calculationService.calculateRBF("UnknownCity", "UnknownVehicleType"));
    }

    @Test
    public void test2() {
        //testCalculateDeliveryFee
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(-5.0);
        weatherData.setWindSpeed(15.0);
        weatherData.setWeatherPhenomenon("rain");
        CalculationService calculationService = new CalculationService();


        double deliveryFee = calculationService.calculateDeliveryFee("Tallinn", "Bike", weatherData);

        // Expected delivery fee calculation:
        // Base fee for "Bike" in "Tallinn" is 3.0
        // Temperature extra fee: 0.5 (because it falls between -10.0 and 0.0)
        // Wind speed extra fee: 0.5 (because it falls between 10.0 and 20.0)
        // Total: 3.0 (base fee) + 0.5 (temperature extra fee) + 0.5 (wind speed extra fee) = 4.0
        double expectedDeliveryFee = 4.5;

        assertEquals(expectedDeliveryFee, deliveryFee, 0.0); // The third argument is the delta for double comparison
    }
    @Test
    public void test3() {
        //CalculateDeliveryFeeWithHighWindSpeedForBike
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(0.0);
        weatherData.setWindSpeed(22.0); // High wind speed
        weatherData.setWeatherPhenomenon(""); // No adverse weather conditions
        CalculationService calculationService = new CalculationService();

        try {
            calculationService.calculateDeliveryFee("Tallinn", "Bike", weatherData);
        } catch (RuntimeException e) {
            assertEquals("Usage of selected vehicle type is forbidden due to high wind speed", e.getMessage());
        }

    }
    @Test
    public void test4() {
        //CalculateDeliveryFeeWithHailForScooter
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(0.0);
        weatherData.setWindSpeed(2.0); // High wind speed
        weatherData.setWeatherPhenomenon("hail"); // No adverse weather conditions
        CalculationService calculationService = new CalculationService();

        try {
            calculationService.calculateDeliveryFee("Tallinn", "Scooter", weatherData);
        } catch (RuntimeException e) {
            assertEquals("Usage of selected vehicle type is forbidden due to adverse weather conditions", e.getMessage());
        }

    }
    @Test
    public void test5() {
        //CalculateDeliveryFeeInTartuByCar
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(15.0); // Mild temperature
        weatherData.setWindSpeed(10.0); // Moderate wind speed
        CalculationService calculationService = new CalculationService();

        double deliveryFee = calculationService.calculateDeliveryFee("Tartu", "Car", weatherData);

        assertEquals(3.5, deliveryFee, 0.0); // No extra fee expected for car in Tartu
    }
    @Test
    public void test6() {
        // CalculateDeliveryFeeWithSnowForBike
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(-5.0);
        weatherData.setWindSpeed(15.0);
        weatherData.setWeatherPhenomenon("snow");
        CalculationService calculationService = new CalculationService();

        double deliveryFee = calculationService.calculateDeliveryFee("Tallinn", "Bike", weatherData);

        double expectedDeliveryFee = 5.0;

        assertEquals(expectedDeliveryFee, deliveryFee, 0.0);
    }

    @Test
    public void test7() {
        // CalculateDeliveryFeeWithRainForScooter
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(10.0);
        weatherData.setWindSpeed(5.0);
        weatherData.setWeatherPhenomenon("rain");
        CalculationService calculationService = new CalculationService();

        double deliveryFee = calculationService.calculateDeliveryFee("Tallinn", "Scooter", weatherData);

        double expectedDeliveryFee = 4.0;

        assertEquals(expectedDeliveryFee, deliveryFee, 0.0);
    }

    @Test
    public void test8() {
        // CalculateDeliveryFeeWithHailForBike
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(0.0);
        weatherData.setWindSpeed(2.0);
        weatherData.setWeatherPhenomenon("hail");
        CalculationService calculationService = new CalculationService();

        try {
            calculationService.calculateDeliveryFee("Tallinn", "Bike", weatherData);
        } catch (RuntimeException e) {
            assertEquals("Usage of selected vehicle type is forbidden due to adverse weather conditions", e.getMessage());
        }
    }

    @Test
    public void test9() {
        // CalculateDeliveryFeeInTartuByScooter
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(20.0); // Warm temperature
        weatherData.setWindSpeed(5.0); // Low wind speed
        CalculationService calculationService = new CalculationService();

        double deliveryFee = calculationService.calculateDeliveryFee("Tartu", "Scooter", weatherData);


        double expectedDeliveryFee = 3.0;

        assertEquals(expectedDeliveryFee, deliveryFee, 0.0);
    }

    @Test
    public void test10() {
        // CalculateDeliveryFeeInTartuByBike
        WeatherData weatherData = new WeatherData();
        weatherData.setAirTemperature(15.0); // Mild temperature
        weatherData.setWindSpeed(10.0); // Moderate wind speed
        CalculationService calculationService = new CalculationService();

        double deliveryFee = calculationService.calculateDeliveryFee("Tartu", "Bike", weatherData);

        double expectedDeliveryFee = 3.0;

        assertEquals(expectedDeliveryFee, deliveryFee, 0.0);
    }


}
