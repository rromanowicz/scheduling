package ex.rr.scheduling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class SettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settingsIdSeq")
    @SequenceGenerator(name = "settingsIdSeq")
    private Integer id;

    @OneToMany(targetEntity = SettingsEntryEntity.class, cascade = CascadeType.ALL)
    List<SettingsEntryEntity> settingsEntries;
}
