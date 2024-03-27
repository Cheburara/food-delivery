package arroba.fooddelivery.Entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "wmo_code")
    private String wmoCode;

    @Column(name = "air_temperature")
    private Double airTemperature;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "weather_phenomenon")
    private String weatherPhenomenon;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}
