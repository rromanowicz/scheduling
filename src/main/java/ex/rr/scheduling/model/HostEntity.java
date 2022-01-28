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
public class HostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hostIdSeq")
    @SequenceGenerator(name = "hostIdSeq")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    private UserEntity host;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = LocationEntity.class, cascade = CascadeType.ALL)
    private List<LocationEntity> location;

    private String description;

}
