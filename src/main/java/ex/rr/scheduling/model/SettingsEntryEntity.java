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
public class SettingsEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settingsEntryIdSeq")
    @SequenceGenerator(name = "settingsEntryIdSeq")
    private Integer id;

    private String type;

    private String subType;

    private String val;

}
