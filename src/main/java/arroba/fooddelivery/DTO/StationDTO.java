package arroba.fooddelivery.DTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class StationDTO {
    private String name;
    private String city;
    private double longitude;
    private double latitude;
}
