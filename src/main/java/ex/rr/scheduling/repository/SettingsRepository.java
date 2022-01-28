package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.SettingsEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<SettingsEntryEntity, Integer> {
}
