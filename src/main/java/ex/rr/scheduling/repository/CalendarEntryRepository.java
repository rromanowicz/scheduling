package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.CalendarEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarEntryRepository extends JpaRepository<CalendarEntryEntity, Integer> {

    Optional<CalendarEntryEntity> findBySessionDate(LocalDate date);

    List<CalendarEntryEntity> findByHoursUsersId(Integer userId);

}
