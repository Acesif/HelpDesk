package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.Priorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priorities,Long> {
}
