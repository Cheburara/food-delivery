package arroba.fooddelivery.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class DeliveryFeeDTO {
    private Long id;
    private Long stationId;
    private Long vehicleTypeId;
    private Long weatherDataId;
    private Double baseFee;
    private Double extraFee;
}
