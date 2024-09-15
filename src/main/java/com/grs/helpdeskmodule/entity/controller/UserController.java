package com.grs.helpdeskmodule.entity.controller;

import com.grs.helpdeskmodule.dto.UserDto;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.entity.service.UserService;
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

    @PostConstruct
    public void startupUser() {
        User user = User.builder()
                .firstName("test")
                .lastName("test")
                .username("testuser")
                .email("test@user.com")
                .phoneNumber("01111111111")
                .officeId("office_1")
                .designation("GRO")
                .build();

        userService.save(user);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .officeId(userDto.getOfficeId())
                .designation(userDto.getDesignation())
                .build();

        User createdUser = userService.save(user);
        UserDto returnedDto = UserDto.builder()
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .username(createdUser.getUsername())
                .email(createdUser.getEmail())
                .phoneNumber(createdUser.getPhoneNumber())
                .officeId(createdUser.getOfficeId())
                .designation(createdUser.getDesignation())
                .createdOn(createdUser.getCreateDate())
                .build();
        return new ResponseEntity<>(returnedDto, HttpStatus.CREATED);
    }
}
