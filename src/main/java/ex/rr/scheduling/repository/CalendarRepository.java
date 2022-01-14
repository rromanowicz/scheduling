package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Integer> {

    Optional<CalendarEntity> findBySessionDate(LocalDate date);

    List<CalendarEntity> findByHoursUsersId(Integer userId);
}
