package ex.rr.scheduling.model;

import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.model.enums.SettingsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settingsIdSeq")
    @SequenceGenerator(name = "settingsIdSeq")
    private Integer id;

    @NotNull
    private Integer locationId;

    private SettingsTypeEnum type;

    private SettingsSubTypeEnum subType;

    private String val;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;

        return new EqualsBuilder().append(type, settings.type).append(subType, settings.subType).append(val, settings.val).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(subType).append(val).toHashCode();
    }
}
