package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "issue_replies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueReplies extends BaseEntity {

    @OneToOne
    public User repliant;

    @ManyToOne
    public Issue parentIssue;

    public String comment;
}
