package arroba.fooddelivery.Service;

import org.springframework.stereotype.Service;

@Service
public class InputValidatorService {

    // Method to validate the city parameter
    public boolean isValidCity(String city) {
        return city != null && (city.equalsIgnoreCase("Tallinn") || city.equalsIgnoreCase("Tartu") || city.equalsIgnoreCase("Pärnu"));
    }

    // Method to validate the vehicle type parameter
    public boolean isValidVehicleType(String vehicleType) {
        return vehicleType != null && (vehicleType.equalsIgnoreCase("Car") || vehicleType.equalsIgnoreCase("Scooter") || vehicleType.equalsIgnoreCase("Bike"));
    }
}
