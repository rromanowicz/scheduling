package ex.rr.scheduling.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class HourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hourIdSeq")
    @SequenceGenerator(name = "hourIdSeq")
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime sessionTime;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @Cascade(ALL)
    private List<UserEntity> users;

    private Integer count;

    public void addUser(UserEntity user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        if(!users.contains(user)) {
            users.add(user);
            count++;
        }
    }

    public void removeUser(UserEntity user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.remove(user);
        count--;
    }
}
