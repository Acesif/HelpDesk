package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/su")
@RequiredArgsConstructor
public class SuperadminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * todo need to create a rest api for getting permissions
     */
    @PutMapping("/update/user")
    public Response<?> updateUser(@RequestBody UserDTO userDTO){

        User existingUser = userService.findUserByEmail(userDTO.getEmail());

        if (existingUser == null){
            return Response.<UserDTO>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("User not found")
                    .data(null)
                    .build();
        }

        existingUser.setFlag(true);
        existingUser.setName(userDTO.getName() == null ? existingUser.getName() : userDTO.getName());
        existingUser.setEmail(userDTO.getEmail() == null ? existingUser.getEmail() : userDTO.getEmail());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber() == null ? existingUser.getPhoneNumber() : userDTO.getPhoneNumber());
        existingUser.setOfficeId(userDTO.getOfficeId() == null ? existingUser.getOfficeId() : userDTO.getOfficeId());
        existingUser.setDesignation(userDTO.getDesignation() == null ? existingUser.getDesignation() : userDTO.getDesignation());
        existingUser.setPassword(userDTO.getPassword() == null ? existingUser.getPassword() : passwordEncoder.encode(userDTO.getPassword()));

        User updatedUser = userService.update(existingUser);

        UserDTO updatedDTO = UserDTO.builder()
                .name(updatedUser.getName())
                .email(updatedUser.getEmail())
                .phoneNumber(updatedUser.getPhoneNumber())
                .officeId(updatedUser.getOfficeId())
                .designation(updatedUser.getDesignation())
                .password(updatedUser.getPassword())
                .createdOn(updatedUser.getCreateDate())
                .build();

        return Response.<UserDTO>builder()
                .status(HttpStatus.CREATED)
                .message("User successfully updated")
                .data(updatedDTO)
                .build();
    }

    
}
