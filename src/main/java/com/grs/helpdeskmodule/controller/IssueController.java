package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.service.IssueService;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.IssueMapper;
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
    public ResponseEntity<IssueDTO> createIssue(@RequestBody IssueDTO issueDto){
        Issue issue = Issue.builder()
                .title(issueDto.getTitle())
                .description(issueDto.getDescription())
                .status(issueDto.getStatus())
                .trackingNumber(issueDto.getTrackingNumber())
                .attachments(IssueMapper.convertToAttachments(issueDto.getAttachments()))
                .postedBy(userService.findUserByEmail(issueDto.getPostedBy()))
                .build();

        Issue savedIssue = issueService.save(issue);
        IssueDTO savedIssueDTO = IssueDTO.builder()
                .title(savedIssue.getTitle())
                .description(savedIssue.getDescription())
                .status(savedIssue.getStatus())
                .trackingNumber(savedIssue.getTrackingNumber())
                .attachments(IssueMapper.convertToAttachmentDTOs(savedIssue.getAttachments()))
                .build();

        return new ResponseEntity<>(savedIssueDTO, HttpStatus.CREATED);
    }
}
