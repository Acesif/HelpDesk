package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.entity.User;

public class UserUtils {

    public static User mapToUser(UserDTO dto){
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .designation(dto.getDesignation())
                .officeId(dto.getOfficeId())
                .phoneNumber(dto.getPhoneNumber())
                .password(dto.getPassword())
                .build();
    }

    public static UserDTO maptoDTO(User user){
        return UserDTO.builder()
                .createdOn(user.getCreateDate())
                .password("***")
                .name(user.getName())
                .email(user.getEmail())
                .designation(user.getDesignation())
                .officeId(user.getOfficeId())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
