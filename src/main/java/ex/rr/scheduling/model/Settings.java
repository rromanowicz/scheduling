package ex.rr.scheduling.model;

import com.fasterxml.jackson.annotation.JsonView;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.model.enums.SettingsTypeEnum;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settingsIdSeq")
    @SequenceGenerator(name = "settingsIdSeq")
    private Integer id;

    @NotNull
    @JsonView(View.ISettings.class)
    private Integer locationId;

    @JsonView(View.ISettings.class)
    private SettingsTypeEnum type;

    @JsonView(View.ISettings.class)
    private SettingsSubTypeEnum subType;

    @JsonView(View.ISettings.class)
    private String val;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Settings settings = (Settings) o;

        return new EqualsBuilder().append(type, settings.type).append(subType, settings.subType)
                .append(val, settings.val)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(subType).append(val).toHashCode();
    }
}
