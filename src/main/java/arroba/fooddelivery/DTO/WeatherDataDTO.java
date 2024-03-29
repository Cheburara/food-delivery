package arroba.fooddelivery.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class WeatherDataDTO {
    private String stationName;
    private String wmoCode;
    private Double airTemperature;
    private Double windSpeed;
    private String weatherPhenomenon;
    private LocalDate timestamp;
}
