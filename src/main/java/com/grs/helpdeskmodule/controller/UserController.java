package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.UserMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @PostMapping("/create")
    public Response<UserDTO> createUser(@RequestBody UserDTO userDto){
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .officeId(userDto.getOfficeId())
                .designation(userDto.getDesignation())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        User createdUser = userService.save(user);
        UserDTO returnedDto = UserDTO.builder()
                .name(createdUser.getName())
                .email(createdUser.getEmail())
                .phoneNumber(createdUser.getPhoneNumber())
                .officeId(createdUser.getOfficeId())
                .designation(createdUser.getDesignation())
                .password("***")
                .createdOn(createdUser.getCreateDate())
                .build();
        return Response.<UserDTO>builder()
                .status(HttpStatus.CREATED)
                .message("User successfully created")
                .data(returnedDto)
                .build();

    }

    @GetMapping("/all")
    public Response<List<UserDTO>> findAllUsers(){
        List<User> userList = userService.findAll();
        List<UserDTO> userDTOList = userList.stream().map(UserMapper::maptoDTO).toList();
        return Response.<List<UserDTO>>builder()
                .status(HttpStatus.OK)
                .message("All users retrieved")
                .data(userDTOList)
                .build();
    }
}
