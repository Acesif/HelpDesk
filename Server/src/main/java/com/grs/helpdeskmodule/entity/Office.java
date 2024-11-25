package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "office")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Office extends BaseEntity {

    private String officeName;
    private String officeAddress;
    private String officePhone;
    private String officeEmail;
}
