package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "issue_replies")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueReplies extends BaseEntity {

    public Long repliantId;
    public Long parentIssueId;
    public String comment;
    @Enumerated(EnumType.STRING)
    public IssueStatus updatedStatus;
}
