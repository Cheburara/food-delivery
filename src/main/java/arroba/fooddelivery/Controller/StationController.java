package arroba.fooddelivery.Controller;

import arroba.fooddelivery.DTO.StationDTO;
import arroba.fooddelivery.Service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

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
}