package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.entity.User;

public interface UserRepository extends BaseEntityRepository<User> {

//    User findByEmail(String email);
    User findByUsername(String username);

    User findByPhoneNumber(String phone_number);
}
