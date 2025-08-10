package com.grs.helpdeskmodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "office")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Office implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    protected Long id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    protected Date createDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    @UpdateTimestamp
    @Column(name = "updated_at")
    protected Date updateDate;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    protected Boolean flag = true;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String office_name_eng;
    private Long office_layer_id;
    private Long office_ministry_id;
    private Long office_origin_id;
    private Long parent_office_id;
}
