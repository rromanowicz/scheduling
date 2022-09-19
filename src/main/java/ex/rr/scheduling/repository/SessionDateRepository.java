package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.SessionDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionDateRepository extends JpaRepository<SessionDay, Integer> {
}
