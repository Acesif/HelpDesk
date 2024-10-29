package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "user",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email","phone_number"})
)
public class User extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Email
    private String email;

    @Column(name = "phone_number")
    @Pattern(regexp = "\\d{11}")
    private String phoneNumber;

    @Column(name = "office_id")
    private Long officeId;

    private String designation;

    private String password;
}
