package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.SessionMonth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface SessionMonthRepository extends JpaRepository<SessionMonth, Integer> {
}
