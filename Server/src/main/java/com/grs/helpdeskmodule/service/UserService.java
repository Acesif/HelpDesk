package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.dto.LoginRequest;
import com.grs.helpdeskmodule.entity.User;


import com.grs.helpdeskmodule.jwt.JWTService;
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
    private final JWTService jwtService;

    public UserService(BaseEntityRepository<User> baseRepository, UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
        super(baseRepository);
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User findUserByPhoneNumber (String phone_number){
        return userRepository.findByPhoneNumber(phone_number);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public String verify(LoginRequest loginRequest) {
        try{
            User user = findUserByUsername(loginRequest.getUsername());

            if (user == null){
                return "User does not exist";
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
            );
            if (authentication.isAuthenticated()){
                return jwtService.generateToken(loginRequest.getUsername(), user);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public String refreshAccessToken(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username);

        return jwtService.generateToken(username, user);
    }
}
