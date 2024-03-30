package arroba.fooddelivery.Mapper;

import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.DTO.WeatherDataDTO;
import arroba.fooddelivery.Entity.Station;
import arroba.fooddelivery.DTO.StationDTO;
import arroba.fooddelivery.Entity.VehicleType;
import arroba.fooddelivery.DTO.VehicleDTO;
import arroba.fooddelivery.Entity.DeliveryFee;
import arroba.fooddelivery.DTO.DeliveryFeeDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component

public class EntityDtoMapper {
    public List<WeatherDataDTO> mapWeatherEntitiesToDTOs(List<WeatherData> entities) {
        return entities.stream().map(this::mapWeatherEntityToDTO).collect(Collectors.toList());
    }

    public WeatherDataDTO mapWeatherEntityToDTO(WeatherData entity) {
        WeatherDataDTO dto = new WeatherDataDTO();
        dto.setStationName(entity.getStationName());
        dto.setWmoCode(entity.getWmoCode());
        dto.setAirTemperature(entity.getAirTemperature());
        dto.setWindSpeed(entity.getWindSpeed());
        dto.setWeatherPhenomenon(entity.getWeatherPhenomenon());
        dto.setTimestamp(entity.getTimestamp().toLocalDate()); // Assuming LocalDateTime to LocalDate conversion
        return dto;
    }
    public List<StationDTO> mapStationEntitiesToDTOs(List<Station> entities) {
        return entities.stream().map(this::mapStationEntityToDTO).collect(Collectors.toList());
    }

    public StationDTO mapStationEntityToDTO(Station entity) {
        StationDTO dto = new StationDTO();
        dto.setName(entity.getName());
        dto.setLongitude(entity.getLongitude());
        dto.setLatitude(entity.getLatitude());
        return dto;
    }
    public List<VehicleDTO> mapVehicleTypeEntitiesToDTOs(List<VehicleType> entities) {
        return entities.stream().map(this::mapVehicleTypeEntityToDTO).collect(Collectors.toList());
    }

    public VehicleDTO mapVehicleTypeEntityToDTO(VehicleType entity) {
        VehicleDTO dto = new VehicleDTO();
        dto.setType(entity.getType());
        return dto;
    }
    public List<DeliveryFeeDTO> mapDeliveryFeeEntitiesToDTOs(List<DeliveryFee> entities) {
        return entities.stream().map(this::mapDeliveryFeeEntityToDTO).collect(Collectors.toList());
    }
    public DeliveryFeeDTO mapDeliveryFeeEntityToDTO(DeliveryFee entity) {
        DeliveryFeeDTO dto = new DeliveryFeeDTO();
        dto.setId(entity.getId());
        dto.setStationId(entity.getStation().getId());
        dto.setVehicleTypeId(entity.getVehicleType().getId());
        dto.setWeatherDataId(entity.getWeatherData().getId());
        dto.setBaseFee(entity.getBaseFee());
        dto.setExtraFee(entity.getExtraFee());
        return dto;
    }
}
