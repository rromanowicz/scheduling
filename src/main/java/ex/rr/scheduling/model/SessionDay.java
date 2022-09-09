package ex.rr.scheduling.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SessionDay {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sessionDateIdSeq")
    @SequenceGenerator(name = "sessionDateIdSeq")
    private Integer id;

    @JsonIgnore
    private Integer sessionMonthId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate sessionDate;

    @OneToMany(mappedBy = "sessionDateId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(ALL)
    private List<Session> sessions;

    @Builder.Default
    private boolean isActive = true;

}
