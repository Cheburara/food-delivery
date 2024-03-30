package arroba.fooddelivery.Service;

import arroba.fooddelivery.Entity.WeatherData;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    public double calculateDeliveryFee(String city, String vehicleType, WeatherData weatherData) {

        double baseFee = calculateRBF(city, vehicleType);

        double extraFee = calculateEFFW(weatherData, vehicleType);

        double totalFee = baseFee + extraFee;

        return totalFee;
    }

    //RegionalBaseFee-RBF
    public double calculateRBF(String city, String vehicleType) {
        // Check the city and vehicle type to determine the regional base fee
        if (city.equalsIgnoreCase("Tallinn")) {
            if (vehicleType.equalsIgnoreCase("Car")) {
                return 4.0;
            } else if (vehicleType.equalsIgnoreCase("Scooter")) {
                return 3.5;
            } else if (vehicleType.equalsIgnoreCase("Bike")) {
                return 3.0;
            }
        } else if (city.equalsIgnoreCase("Tartu")) {
            if (vehicleType.equalsIgnoreCase("Car")) {
                return 3.5;
            } else if (vehicleType.equalsIgnoreCase("Scooter")) {
                return 3.0;
            } else if (vehicleType.equalsIgnoreCase("Bike")) {
                return 2.5;
            }
        } else if (city.equalsIgnoreCase("Pärnu")) {
            if (vehicleType.equalsIgnoreCase("Car")) {
                return 3.0;
            } else if (vehicleType.equalsIgnoreCase("Scooter")) {
                return 2.5;
            } else if (vehicleType.equalsIgnoreCase("Bike")) {
                return 2.0;
            }
        }

        return 0.0;
    }

    //ExtraFeesForWeather-EFFW
    public double calculateEFFW(WeatherData weatherData, String vehicleType) {
        // Check if the vehicle type is Scooter or Bike
        if (vehicleType.equalsIgnoreCase("Scooter") || vehicleType.equalsIgnoreCase("Bike")) {
            double extraFee = 0.0;

            // Check weather conditions
            String weatherPhenomenon = weatherData.getWeatherPhenomenon();
            double airTemperature = weatherData.getAirTemperature();
            double windSpeed = weatherData.getWindSpeed();

//             Calculate extra fee based on air temperature
            if (airTemperature < -10.0) {
                extraFee += 1.0; // ATEF = 1 €
            } else if (airTemperature >= -10.0 && airTemperature < 0.0) {
                extraFee += 0.5; // ATEF = 0.5 €
            }
            // test
//            if (airTemperature >= 0.0) {
//                extraFee += 5.0; // ATEF = 5 € for air temperature of 0°C or higher
//            }

            // Calculate extra fee based on wind speed (only for Bike)
            if (vehicleType.equalsIgnoreCase("Bike")) {
                if (windSpeed >= 10.0 && windSpeed <= 20.0) {
                    extraFee += 0.5;
                } else if (windSpeed > 20.0) {
                    throw new RuntimeException("Usage of selected vehicle type is forbidden due to high wind speed");
                }
            }

            // Calculate extra fee based on weather phenomenon
            if (weatherPhenomenon != null) {
                switch (weatherPhenomenon.toLowerCase()) {
                    case "snow":
                    case "sleet":
                        extraFee += 1.0; // WPEF = 1 €
                        break;
                    case "rain":
                        extraFee += 0.5; // WPEF = 0.5 €
                        break;
                    case "glaze":
                    case "hail":
                    case "thunder":
                        throw new RuntimeException("Usage of selected vehicle type is forbidden due to adverse weather conditions");
                }
            }

            return extraFee;
        }

        // Return 0 if no extra fee is applicable
        return 0.0;
    }

}
