package arroba.fooddelivery.Repository;

import  arroba.fooddelivery.Entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherDataRepository extends JpaRepository <WeatherData, Long> {
    WeatherData findTopByCityOrderByTimestampDesc(String city);
}
