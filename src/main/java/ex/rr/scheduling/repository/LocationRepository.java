package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
