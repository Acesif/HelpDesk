package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permissions,Long> {
}
