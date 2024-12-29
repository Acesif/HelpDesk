package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.IssueRepliesDTO;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueReplies;

public class IssueUtils {
    public static IssueDTO convertToIssueDTO(Issue issue){
        return IssueDTO.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .status(issue.getStatus())
                .officeId(issue.getOffice().getId())
                .postedOn(issue.getCreateDate())
                .updatedOn(issue.getUpdateDate())
                .postedBy(issue.getPostedBy().getId())
                .trackingNumber(issue.getTrackingNumber())
                .description(issue.getDescription())
                .category(issue.getIssueCategory())
                .build();
    }

    public static IssueRepliesDTO convertToIssueRepliesDTO(IssueReplies issueReplies){
        return IssueRepliesDTO.builder()
                .repliantId(issueReplies.getRepliantId())
                .parentIssueId(issueReplies.getParentIssueId())
                .comment(issueReplies.getComment())
                .updatedStatus(issueReplies.getUpdatedStatus())
                .updateDate(issueReplies.getUpdateDate())
                .build();
    }

    public static IssueReplies convertToIssueReplies(IssueRepliesDTO issueRepliesDTO){
        return IssueReplies.builder()
                .repliantId(issueRepliesDTO.getRepliantId())
                .parentIssueId(issueRepliesDTO.getParentIssueId())
                .comment(issueRepliesDTO.getComment())
                .updatedStatus(issueRepliesDTO.getUpdatedStatus())
                .build();
    }
}
