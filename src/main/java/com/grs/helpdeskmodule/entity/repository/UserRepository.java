package com.grs.helpdeskmodule.entity.repository;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.entity.User;

public interface UserRepository extends BaseEntityRepository<User> {

    User findByUsername(String username);
}
