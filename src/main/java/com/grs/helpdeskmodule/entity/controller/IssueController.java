package com.grs.helpdeskmodule.entity.controller;

import com.grs.helpdeskmodule.dto.IssueDto;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.service.IssueService;
import com.grs.helpdeskmodule.entity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;

    @Transactional
    @PostMapping("/new")
    public ResponseEntity<IssueDto> createIssue(@RequestBody IssueDto issueDto){
        Issue issue = Issue.builder()
                .title(issueDto.getTitle())
                .description(issueDto.getDescription())
                .status(issueDto.getStatus())
                .trackingNumber(issueDto.getTrackingNumber())
                .attachments(issueDto.getAttachments())
                .postedBy(userService.findUserByUsername(issueDto.getPostedBy()))
                .build();

        Issue savedIssue = issueService.save(issue);
        IssueDto savedIssueDto = IssueDto.builder()
                .title(savedIssue.getTitle())
                .description(savedIssue.getDescription())
                .status(savedIssue.getStatus())
                .postedBy(savedIssue.getPostedBy().getUsername())
                .trackingNumber(savedIssue.getTrackingNumber())
                .attachments(savedIssue.getAttachments())
                .build();

        return new ResponseEntity<>(savedIssueDto, HttpStatus.CREATED);
    }
}
