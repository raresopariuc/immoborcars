package com.raresopariuc.licenta.repository;

import com.raresopariuc.licenta.model.Role;
import com.raresopariuc.licenta.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
