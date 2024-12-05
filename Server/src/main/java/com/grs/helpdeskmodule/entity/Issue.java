package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "issue")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Issue extends BaseEntity {

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments;
}
