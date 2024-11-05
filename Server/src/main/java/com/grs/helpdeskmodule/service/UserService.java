package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.dto.LoginRequest;
import com.grs.helpdeskmodule.entity.User;


import com.grs.helpdeskmodule.jwt.JWTService;
import com.grs.helpdeskmodule.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService extends BaseService<User> {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserService(BaseEntityRepository<User> baseRepository, UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
        super(baseRepository);
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
            User user = findUserByEmail(loginRequest.getEmail());

            if (user == null){
                return "User does not exist";
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()){
                return jwtService.generateToken(loginRequest.getEmail(), user);
            }
        } catch (Exception e){
            return (Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
