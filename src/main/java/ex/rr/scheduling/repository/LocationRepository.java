package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
