package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

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

    @JsonIgnore
    @NotNull
    private Integer locationId;

    private Integer sessionYear;

    @OneToMany(mappedBy = "sessionYearId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SessionMonth> sessionMonths;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SessionYear that = (SessionYear) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
