package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
