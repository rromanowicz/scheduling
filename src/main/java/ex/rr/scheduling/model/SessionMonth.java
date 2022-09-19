package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SessionMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionMonthIdSeq")
    @SequenceGenerator(name = "sessionMonthIdSeq")
    private Integer id;

    @JsonIgnore
    @NotNull
    private Integer sessionYearId;

    private Month monthName;

    private LocalDate monthDate;

    @OneToMany(mappedBy = "sessionMonthId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SessionDay> sessionDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SessionMonth that = (SessionMonth) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
