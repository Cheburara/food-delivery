package arroba.fooddelivery.Service;

import arroba.fooddelivery.Entity.VehicleType;
import arroba.fooddelivery.Repository.VehicleTypeRepository;
import arroba.fooddelivery.Mapper.EntityDtoMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;
    public VehicleTypeService(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @PostConstruct
    public void initializeVehicleTypes() {
        List<VehicleType> types = Arrays.asList(
                new VehicleType("Car"),
                new VehicleType("Scooter"),
                new VehicleType("Bike")
        );
        vehicleTypeRepository.saveAll(types);
    }
    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }

}
