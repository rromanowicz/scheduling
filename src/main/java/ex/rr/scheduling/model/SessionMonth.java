package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Month;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "sessionMonthId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionDay> sessionDays;

}
