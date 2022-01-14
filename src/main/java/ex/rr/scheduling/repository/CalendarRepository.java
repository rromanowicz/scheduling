package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Integer> {

    Optional<CalendarEntity> findBySessionDateAndSessionTime(LocalDate date, LocalTime time);
    Optional<CalendarEntity> findBySessionDate(LocalDate date);
    Optional<CalendarEntity> findBySessionTime(LocalTime time);

    List<CalendarEntity> findByUsersId(Integer userId);
}
