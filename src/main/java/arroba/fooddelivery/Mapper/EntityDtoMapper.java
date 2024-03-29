package arroba.fooddelivery.Mapper;

import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.DTO.WeatherDataDTO;
import arroba.fooddelivery.Entity.Station;
import arroba.fooddelivery.DTO.StationDTO;
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
}
