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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User postedBy;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments;

    @OneToMany(mappedBy = "parentIssue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueReplies> issueReplies;


    @PrePersist
    public void generateTrackingNumber() {
        SecureRandom random = new SecureRandom();
        long number = (long) (random.nextDouble() * 1_000_000_00000L);
        this.trackingNumber = String.format("%011d", number);
    }
}
