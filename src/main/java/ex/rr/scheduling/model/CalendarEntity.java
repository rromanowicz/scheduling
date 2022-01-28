package ex.rr.scheduling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendarIdSeq")
    @SequenceGenerator(name = "calendarIdSeq")
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = CalendarEntryEntity.class, cascade = CascadeType.ALL)
    List<CalendarEntryEntity> calendarEntries;

}
