package com.grs.helpdeskmodule.controller;


import com.grs.helpdeskmodule.dto.IssueRepliesDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueReplies;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.IssueReplyService;
import com.grs.helpdeskmodule.service.IssueService;
import com.grs.helpdeskmodule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("/api/issue_reply")
@RequiredArgsConstructor
public class IssueReplyController {

    private final IssueService issueService;
    private final UserService userService;
    private final IssueReplyService issueReplyService;

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
                    .parentIssue(issue)
                    .comment(issueRepliesDTO.getComment())
                    .repliant(postedByUser)
                    .updatedStatus(issueRepliesDTO.getUpdatedStatus())
                    .build();

            IssueReplies savedIssueReply = issueReplyService.saveReply(issueReply);
            IssueRepliesDTO repliesDTO = IssueRepliesDTO.builder()
                    .repliant(savedIssueReply.getRepliant())
                    .comment(savedIssueReply.getComment())
                    .parentIssue(savedIssueReply.getParentIssue())
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
