package ex.rr.scheduling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locationIdSeq")
    @SequenceGenerator(name = "locationIdSeq")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = LocationDetailsEntity.class, cascade = CascadeType.ALL)
    private LocationDetailsEntity locationDetails;

    @OneToOne(targetEntity = CalendarEntity.class, cascade = CascadeType.ALL)
    private CalendarEntity calendar;

    @OneToOne(targetEntity = SettingsEntity.class, cascade = CascadeType.ALL)
    private SettingsEntity settings;

}
