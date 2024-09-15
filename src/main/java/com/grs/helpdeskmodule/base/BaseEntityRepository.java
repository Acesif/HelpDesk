package com.grs.helpdeskmodule.base;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseEntityRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}
