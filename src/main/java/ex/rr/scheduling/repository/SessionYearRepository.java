package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.SessionYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionYearRepository extends JpaRepository<SessionYear, Integer> {

    Optional<SessionYear> findByLocationIdAndSessionYear(Integer locationId, Integer year);
}
