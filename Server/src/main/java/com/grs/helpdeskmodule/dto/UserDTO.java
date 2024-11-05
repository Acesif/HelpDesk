package com.grs.helpdeskmodule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long officeId;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    private Date createdOn;
    private String designation;
    private String password;
}
