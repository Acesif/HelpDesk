package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, String> {
}
