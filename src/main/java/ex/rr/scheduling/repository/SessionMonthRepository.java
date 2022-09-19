package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.SessionMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionMonthRepository extends JpaRepository<SessionMonth, Integer> {
}
