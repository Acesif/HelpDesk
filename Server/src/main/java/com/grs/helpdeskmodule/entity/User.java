package com.grs.helpdeskmodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "user",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email","phone_number"})
)
public class User implements Serializable {

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