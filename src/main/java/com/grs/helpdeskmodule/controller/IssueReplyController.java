package com.grs.helpdeskmodule.controller;


import com.grs.helpdeskmodule.base.BaseEntity;
import com.grs.helpdeskmodule.dto.IssueRepliesDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueReplies;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.IssueReplyService;
import com.grs.helpdeskmodule.service.IssueService;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/issue_reply")
@RequiredArgsConstructor
public class IssueReplyController {

    private final IssueService issueService;
    private final UserService userService;
    private final IssueReplyService issueReplyService;

    private void setCurrentStatus(Long parentId){
        IssueStatus currentStatus = issueReplyService.findLastReply(parentId).getUpdatedStatus();
        Issue updatedIssue = issueService.findById(parentId);
        updatedIssue.setStatus(currentStatus);
        issueService.update(updatedIssue);
    }

    @GetMapping("/{id}")
    public Response<?> getIssueHistory(@PathVariable("id") Long id){
        List<IssueReplies> getIssuesById = issueReplyService.findIssuesByParentId(id);

        List<IssueReplies> issueRepliesList = getIssuesById.stream()
                .sorted(Comparator.comparing(BaseEntity::getUpdateDate))
                .toList();

        List<IssueRepliesDTO> issueRepliesDTOS = issueRepliesList.stream().map(IssueMapper::convertToIssueRepliesDTO).toList();

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issue history of issue id "+id+" has been retrieved")
                .data(issueRepliesDTOS)
                .build();
    }

    @PostMapping("/{id}")
    public Response<?> postReply(
            @PathVariable("id") Long id,
            @RequestBody IssueRepliesDTO issueRepliesDTO
    ){

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User postedByUser = userService.findUserByEmail(loggedInUserEmail);

        Issue issue = issueService.findById(id);

        if (issue != null){
            IssueReplies issueReply = IssueReplies.builder()
                    .flag(true)
                    .parentIssueId(issue.getId())
                    .comment(issueRepliesDTO.getComment())
                    .repliantId(postedByUser.getId())
                    .updatedStatus(issueRepliesDTO.getUpdatedStatus())
                    .build();

            IssueReplies savedIssueReply = issueReplyService.saveReply(issueReply);
            setCurrentStatus(id);

            IssueRepliesDTO repliesDTO = IssueRepliesDTO.builder()
                    .repliantId(savedIssueReply.getRepliantId())
                    .comment(savedIssueReply.getComment())
                    .parentIssueId(savedIssueReply.getParentIssueId())
                    .updatedStatus(savedIssueReply.getUpdatedStatus())
                    .build();

            return Response.<IssueRepliesDTO>builder()
                    .status(HttpStatus.CREATED)
                    .message("Reply successful")
                    .data(repliesDTO)
                    .build();

        } else {
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Issue not found")
                    .data(null)
                    .build();
        }
    }
}
