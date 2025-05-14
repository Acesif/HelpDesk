package com.grs.helpdeskmodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "office")
@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Office {

    @Id
    private Long id;
    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String office_name_eng;
    private Long office_layer_id;
    private Long office_ministry_id;
    private Long office_origin_id;
    private Long parent_office_id;
    private Long office_unit_organogram_id;
}