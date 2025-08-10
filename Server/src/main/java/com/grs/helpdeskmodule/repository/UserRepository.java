package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByPhoneNumber(String phone_number);
}