package com.grs.helpdeskmodule.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permission")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permissions {

    @Id
    private Long id;
    private String permission;
}
