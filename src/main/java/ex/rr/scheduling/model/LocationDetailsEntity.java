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
public class LocationDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locationIdSeq")
    @SequenceGenerator(name = "locationIdSeq")
    private Integer id;

    private String city;

    private String postCode;

    private String address;

    private String description;
}
