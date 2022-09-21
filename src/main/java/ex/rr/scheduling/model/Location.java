package ex.rr.scheduling.model;


import static org.hibernate.annotations.CascadeType.ALL;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locationIdSeq")
    @SequenceGenerator(name = "locationIdSeq")
    private Integer id;

    @Column(unique = true, nullable = false)
    @JsonView(View.ILocation.class)
    private String address;

    @OneToMany(mappedBy = "locationId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(ALL)
    @ToString.Exclude
    @JsonView(View.ILocation.class)
    private Set<SessionYear> sessionYears;

    @OneToMany(mappedBy = "locationId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonView(View.ISettings.class)
    private Set<Settings> settings;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Location location = (Location) o;
        return id != null && Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
