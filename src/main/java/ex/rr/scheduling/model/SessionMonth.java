package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDate;
import java.time.Month;
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
import org.hibernate.annotations.Where;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Where(clause = "month_date >= DATEADD('DD',-DAY(DATEADD('M',1,CURRENT_DATE()))+1,CURRENT_DATE())")
public class SessionMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionMonthIdSeq")
    @SequenceGenerator(name = "sessionMonthIdSeq")
    private Integer id;

    @NotNull
    private Integer sessionYearId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonView(View.ISessionMonth.class)
    private Month monthName;

    private LocalDate monthDate;

    @OneToMany(mappedBy = "sessionMonthId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonView(View.ISessionMonth.class)
    private List<SessionDay> sessionDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        SessionMonth that = (SessionMonth) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
