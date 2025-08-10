package com.grs.helpdeskmodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "issue")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Issue implements Serializable {

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

    @Column(unique = true)
    private String trackingNumber;

    @Nonnull
    private String title;

    @Nonnull
    private String description;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private IssueCategory issueCategory;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Office office;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments;
}