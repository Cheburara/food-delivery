package arroba.fooddelivery.Entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class DeliveryFee {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;

    @OneToOne
    @JoinColumn(name = "weather_data_id")
    private WeatherData weatherData;

    @Column(name = "base_fee")
    private Double baseFee;

    @Column(name = "extra_fee")
    private Double extraFee;
}
