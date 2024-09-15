package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "issue")
@Getter
@Setter
@Builder
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "issue_id")
    private Set<Attachment> attachments;

    @PrePersist
    public void generateTrackingNumber() {
        SecureRandom random = new SecureRandom();
        long number = (long) (random.nextDouble() * 1_000_000_00000L);
        this.trackingNumber = String.format("%011d", number);
    }
}
