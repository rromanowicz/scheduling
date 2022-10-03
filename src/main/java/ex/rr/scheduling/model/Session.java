package ex.rr.scheduling.model;

import static org.hibernate.annotations.CascadeType.ALL;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;

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
    @JsonView(View.ISession.class)
    private Integer id;

    private Integer sessionDayId;

    @JsonView(View.ISession.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime sessionTime;

    @JsonView(View.ISession.class)
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = User.class)
    @Cascade(ALL)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<User> users;

    @JsonView(View.ISession.class)
    @Formula("SELECT COUNT(1) FROM SESSION_USERS SU WHERE SU.SESSION_ID=id")
    private Integer count;

    @JsonView(View.ISession.class)
    @Builder.Default
    private boolean active = true;

    public void addUser(User user) {
        if (users == null) {
            users = new HashSet<>();
        }
        users.add(user);
    }

    public void removeUser(User user) {
        if (users == null) {
            users = new HashSet<>();
        }
        users.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Session session = (Session) o;
        return id != null && Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
