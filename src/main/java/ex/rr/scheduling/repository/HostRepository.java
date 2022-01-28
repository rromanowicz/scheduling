package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.HostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<HostEntity, Integer> {

    Optional<HostEntity> findByHostId(Integer hostId);

}
