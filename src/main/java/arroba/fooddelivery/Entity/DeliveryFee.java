package arroba.fooddelivery.Entity;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class DeliveryFee {

    public DeliveryFee(Station station, VehicleType vehicleType, WeatherData weatherData, Double baseFee, Double extraFee) {
        this.station = station;
        this.vehicleType = vehicleType;
        this.weatherData = weatherData;
        this.baseFee = baseFee;
        this.extraFee = extraFee;
    }

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
