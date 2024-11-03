package com.grs.helpdeskmodule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueRepliesDTO {
    public Long repliantId;
    public Long parentIssueId;
    public String comment;
    public IssueStatus updatedStatus;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    private Date updateDate;
}
