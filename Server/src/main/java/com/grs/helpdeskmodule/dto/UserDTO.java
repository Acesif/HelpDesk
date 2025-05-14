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
    private String username;
    private Long officeId;
    private String phoneNumber;
    private String role;
    private String password;
    private Long officeMinistryId;
    private Long officeOriginId;
    private String designation;
    private Long employeeRecordId;
    private Long officeUnitOrganogramId;
    private Long layerLevel;
    private String officeNameBangla;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    private Date createdOn;
}
