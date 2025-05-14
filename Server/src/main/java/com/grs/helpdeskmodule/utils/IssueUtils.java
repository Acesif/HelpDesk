package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.base.BaseEntity;
import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.IssueRepliesDTO;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueReplies;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;

public class IssueUtils extends CommonUtils{

    public static List<IssueDTO> convertToIssueDTOList(List<Issue> issues) {
        return issues.stream().map(IssueUtils::convertToIssueDTO).toList();
    }

    public static List<IssueDTO> responseIssueListDTO(Page<Issue> issueList) {
        List<Issue> sortedIssueDTOList = IssueUtils.sortIssues(issueList);
        List<IssueDTO> issueDTOList = IssueUtils.convertToIssueDTOList(sortedIssueDTOList);
        Map<Long, Issue> issueMap = issueList.stream().collect(Collectors.toMap(Issue::getId, issue -> issue));
        List<Map<String, Object>> transformedAttachments = new ArrayList<>();

        for (IssueDTO issueDTO : issueDTOList) {
            Issue correspondingIssue = issueMap.get(issueDTO.getId());
            if (correspondingIssue != null) {
                parseAttachment(correspondingIssue, transformedAttachments);
                issueDTO.setAttachments(transformedAttachments);
            }
        }
        return issueDTOList;
    }

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

    public static List<Issue> sortIssues(Page<Issue> issues){
        return issues.stream().sorted(Comparator.comparing(BaseEntity::getUpdateDate)).toList();
    }
}
