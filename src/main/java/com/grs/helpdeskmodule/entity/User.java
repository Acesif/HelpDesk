package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Email
    private String email;

    @Column(name = "phone_number")
    @Pattern(regexp = "\\d{11}")
    private String phoneNumber;

    @Column(name = "office_id")
    private String officeId;

    private String designation;
}
