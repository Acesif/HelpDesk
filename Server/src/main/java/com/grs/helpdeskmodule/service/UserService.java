package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.dto.LoginRequest;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.jwt.JWTService;
import com.grs.helpdeskmodule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User entity) {
        entity.setCreateDate(new Date());
        entity.setFlag(true);
        return userRepository.saveAndFlush(entity);
    }

    public User update(User entity) {
        entity.setUpdateDate(new Date());
        return userRepository.save(entity);
    }

    public void delete(Long id) {
        User entity = userRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setFlag(false);
            update(entity);
        }
    }

    public void hardDelete(Long id) {
        userRepository.deleteById(id);
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
            throw new RuntimeException(e);
        }
        return null;
    }

    public String refreshAccessToken(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email);

        return jwtService.generateToken(email, user);
    }
}
