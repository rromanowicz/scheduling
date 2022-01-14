package ex.rr.scheduling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settingsIdSeq")
    @SequenceGenerator(name = "settingsIdSeq")
    private Integer id;

    private String type;

    private String subType;

    private String val;

}
