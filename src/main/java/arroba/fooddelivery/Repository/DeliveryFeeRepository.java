package arroba.fooddelivery.Repository;

import arroba.fooddelivery.Entity.DeliveryFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {

}