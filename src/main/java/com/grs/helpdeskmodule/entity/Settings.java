package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settings {

    @Id
    @Column(name = "`key`", nullable = false, unique = true)
    private String key;

    @Column(name = "`value`", nullable = false)
    private String value;

}
