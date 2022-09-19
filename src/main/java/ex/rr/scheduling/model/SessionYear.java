package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SessionYear {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionMonthIdSeq")
    @SequenceGenerator(name = "sessionMonthIdSeq")
    private Integer id;

    @NotNull
    private Integer locationId;

    @JsonView(View.ISessionYear.class)
    private Integer sessionYear;

    @OneToMany(mappedBy = "sessionYearId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonView(View.ISessionYear.class)
    private List<SessionMonth> sessionMonths;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SessionYear that = (SessionYear) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
