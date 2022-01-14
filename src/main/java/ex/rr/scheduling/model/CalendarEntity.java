package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate sessionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime sessionTime;

//    @OneToMany(targetEntity = DayEntity.class)
//    @Cascade(ALL)
//    private List<DayEntity> days;

    @OneToMany(targetEntity = UserEntity.class)
    @Cascade(ALL)
    private List<UserEntity> users;

    public void addUser(UserEntity user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void removeUser(UserEntity user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.remove(user);
    }

//    public void addDay(DayEntity day) {
//        if (days == null) {
//            days = new ArrayList<>();
//        }
//        days.add(day);
//    }
//
//    public void removeDay(DayEntity day) {
//        if (days == null) {
//            days = new ArrayList<>();
//        }
//        days.remove(day);
//    }

}
