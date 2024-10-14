package com.grs.helpdeskmodule.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {


    private String name;

    private String email;

    private String phoneNumber;

    private String officeId;

    private Date createdOn;

    private String designation;

    private String password;
}
