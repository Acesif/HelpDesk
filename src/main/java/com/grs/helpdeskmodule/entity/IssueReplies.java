package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "issue_replies")
public class IssueReplies extends BaseEntity {

    public User repliant;
    public Issue parentIssue;
    public String comment;

}
