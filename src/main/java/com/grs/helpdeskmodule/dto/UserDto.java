package com.grs.helpdeskmodule.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {


    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private String officeId;

    private Date createdOn;

    private String designation;
}
