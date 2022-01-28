package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CalendarEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "calendarEntryIdSeq")
    @SequenceGenerator(name = "calendarEntryIdSeq")
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate sessionDate;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = HourEntity.class, cascade = CascadeType.ALL)
    private List<HourEntity> hours;

    public void addHour(HourEntity hour) {
        if (hours == null) {
            hours = new ArrayList<>();
        }
        hours.add(hour);
    }

    public void removeHour(HourEntity hour) {
        if (hours == null) {
            hours = new ArrayList<>();
        }
        hours.remove(hour);
    }

}
