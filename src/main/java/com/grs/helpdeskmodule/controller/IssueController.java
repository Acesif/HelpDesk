package com.grs.helpdeskmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.IssueService;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;

    @Transactional
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<IssueDTO> createIssue(
            @RequestPart("issue") String issueJson,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        IssueDTO issueDto = objectMapper.readValue(issueJson, IssueDTO.class);

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User postedByUser = userService.findUserByEmail(loggedInUserEmail);

        Issue issue = Issue.builder()
                .flag(true)
                .title(issueDto.getTitle())
                .description(issueDto.getDescription())
                .status(issueDto.getStatus())
                .trackingNumber(issueDto.getTrackingNumber())
                .postedBy(postedByUser)
                .build();

        if (attachments != null && !attachments.isEmpty()) {
            Set<Attachment> attachmentEntities = attachments.stream().map(file -> {
                try {
                    return Attachment.builder()
                            .flag(true)
                            .fileName(file.getOriginalFilename())
                            .fileData(file.getBytes())
                            .issue(issue)
                            .build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());

            issue.setAttachments(attachmentEntities);
        }

        Issue savedIssue = issueService.save(issue);

        IssueDTO savedIssueDTO = IssueDTO.builder()
                .title(savedIssue.getTitle())
                .description(savedIssue.getDescription())
                .status(savedIssue.getStatus())
                .trackingNumber(savedIssue.getTrackingNumber())
                .attachments(IssueMapper.convertToAttachmentDTOs(savedIssue.getAttachments() != null ? savedIssue.getAttachments() : new HashSet<>()))
                .postedOn(savedIssue.getCreateDate())
                .postedBy(savedIssue.getPostedBy().getEmail())
                .build();

        return new ResponseEntity<>(savedIssueDTO, HttpStatus.CREATED);
    }

}
