package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto){
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .officeId(userDto.getOfficeId())
                .designation(userDto.getDesignation())
                .build();

        User createdUser = userService.save(user);
        UserDTO returnedDto = UserDTO.builder()
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .phoneNumber(createdUser.getPhoneNumber())
                .officeId(createdUser.getOfficeId())
                .designation(createdUser.getDesignation())
                .createdOn(createdUser.getCreateDate())
                .build();
        return new ResponseEntity<>(returnedDto, HttpStatus.CREATED);
    }
}
