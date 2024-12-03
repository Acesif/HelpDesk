package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SettingsRepository extends JpaRepository<Settings,String> {

    @Query(value = "SELECT * FROM helpdesk.settings WHERE `key` = :key", nativeQuery = true)
    Settings findByKey(String key);
}
