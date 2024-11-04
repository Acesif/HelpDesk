package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.dto.LoginRequest;
import com.grs.helpdeskmodule.entity.User;


import com.grs.helpdeskmodule.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService<User> {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserService(BaseEntityRepository<User> baseRepository, UserRepository userRepository, AuthenticationManager authenticationManager) {
        super(baseRepository);
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
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

    public String verify(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()){
                return loginRequest.getEmail() +" successfully Logged in";
            }
        } catch (Exception e){
            if (userRepository.findByEmail(loginRequest.getEmail()) == null){
                return "User does not exist";
            }
            return loginRequest.getEmail() + " wrong credentials";
        }
        return null;
    }
}
