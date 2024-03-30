package arroba.fooddelivery.Controller;

import arroba.fooddelivery.DTO.StationDTO;

import arroba.fooddelivery.Service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // method to manually trigger importStationData
    @GetMapping("/import")
    public ResponseEntity<String> importStationData() {
        try {
            stationService.importStationData();
            return ResponseEntity.ok("Station data imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import station data: " + e.getMessage());
        }
    }
    // method to retrieving all station data -- testing purpose
    @GetMapping("/all")
    public ResponseEntity<List<StationDTO>> getAllStationData() {
        List<StationDTO> stationList =stationService.getAllStationData();
        return ResponseEntity.ok(stationList);
    }
}