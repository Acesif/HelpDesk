package com.grs.helpdeskmodule.dto;

import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueRepliesDTO {
    public User repliant;
    public Issue parentIssue;
    public String comment;
    public IssueStatus updatedStatus;
}
