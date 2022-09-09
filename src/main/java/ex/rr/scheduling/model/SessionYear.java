package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<SessionMonth> sessionMonths;

}
