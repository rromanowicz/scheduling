package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.model.enums.SettingsTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    Collection<Settings> findByLocationId(Integer id);

    Collection<Settings> findByLocationIdAndTypeAndSubType(Integer id, SettingsTypeEnum type, SettingsSubTypeEnum subType);

    Collection<Settings> findByLocationIdAndSubType(Integer id, SettingsSubTypeEnum subType);

}
