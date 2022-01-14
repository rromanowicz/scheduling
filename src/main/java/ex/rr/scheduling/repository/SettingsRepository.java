package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.SettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<SettingsEntity, Integer> {
}
