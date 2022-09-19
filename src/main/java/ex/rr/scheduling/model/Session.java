package ex.rr.scheduling.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.*;

import static org.hibernate.annotations.CascadeType.ALL;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionIdSeq")
    @SequenceGenerator(name = "sessionIdSeq")
    private Integer id;

    private Integer sessionDateId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime sessionTime;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = User.class)
    @Cascade(ALL)
    private Set<User> users;

    @Builder.Default
    private Integer count = 0;

    public void addUser(User user) {
        if (users == null) {
            users = new HashSet<>();
        }
        if (!users.contains(user)) {
            users.add(user);
            count++;
        }
    }

    public void removeUser(User user) {
        if (users == null) {
            users = new HashSet<>();
        }
        if (users.contains(user)) {
            users.remove(user);
            count--;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Session session = (Session) o;
        return id != null && Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
