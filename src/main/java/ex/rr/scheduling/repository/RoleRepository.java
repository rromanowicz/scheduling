package ex.rr.scheduling.repository;

import ex.rr.scheduling.model.Role;
import ex.rr.scheduling.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Optional<Role> findByName(RoleEnum roleUser);
}
