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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdSeq")
    @SequenceGenerator(name = "userIdSeq")
    private Integer id;

    private String username;

    private String password;

    private String email;

    private RoleEnum role;


//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "users", targetEntity = CalendarEntity.class, cascade = CascadeType.MERGE)
//    private List<CalendarEntity> calendarEntries;

}
