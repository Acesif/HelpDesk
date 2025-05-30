package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.dto.UserDTO;
import com.grs.helpdeskmodule.dto.UserInformation;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.repository.OfficeRepository;
import com.grs.helpdeskmodule.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    private static UserService userService;
    private static OfficeRepository officeRepository;

    public UserUtils(OfficeRepository officeRepository, UserService userService) {
        UserUtils.officeRepository = officeRepository;
        UserUtils.userService = userService;
    }

    public User mapToUser(UserDTO dto){
        return User.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .designation(dto.getDesignation())
                .office(officeRepository.findById(dto.getOfficeId()).orElse(null))
                .password(dto.getPassword())
                .build();
    }

    public UserDTO maptoDTO(User user){
        return UserDTO.builder()
                .createdOn(user.getCreateDate())
                .password("***")
                .name(user.getName())
                .username(user.getUsername())
                .designation(user.getDesignation())
                .officeId(user.getOffice() != null ? user.getOffice().getId() : null)
                .build();
    }
    public UserInformation extractUserInformation(Authentication authentication){
//        User user = userService.findUserByEmail(
//                SecurityContextHolder.getContext().getAuthentication().getName()
//        );
        User user = userService.findUserByUsername(
                authentication.getName()
        );
        return UserInformation.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .officeId(user.getOffice() != null ? user.getOffice().getId() : null)
                .designation(user.getDesignation())
                .build();
    }
}
