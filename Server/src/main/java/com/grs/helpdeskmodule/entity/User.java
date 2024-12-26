package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @JoinColumn(name = "office_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    private String designation;

    private String password;
}
