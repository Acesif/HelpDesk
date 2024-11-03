package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.User;


import com.grs.helpdeskmodule.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService<User> {

    private final UserRepository userRepository;

    public UserService(BaseEntityRepository<User> baseRepository, UserRepository userRepository) {
        super(baseRepository);
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findUserByPhoneNumber (String phone_number){
        return userRepository.findByPhoneNumber(phone_number);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
