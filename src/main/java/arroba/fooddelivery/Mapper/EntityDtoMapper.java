package arroba.fooddelivery.Mapper;

import arroba.fooddelivery.Entity.WeatherData;
import arroba.fooddelivery.DTO.WeatherDataDTO;
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
}
