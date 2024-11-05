package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings,String> {
}
