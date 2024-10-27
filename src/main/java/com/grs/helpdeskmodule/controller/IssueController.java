package com.grs.helpdeskmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.service.IssueService;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.NullLiteral;
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
    public Response<?> createIssue(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("status") String status,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        IssueDTO issueDto = IssueDTO.builder()
                .title(title)
                .description(description)
                .status(IssueStatus.valueOf(status))
                .build();

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
                .postedOn(savedIssue.getCreateDate())
                .postedBy(savedIssue.getPostedBy().getId())
                .build();

        return Response.builder()
                .status(HttpStatus.CREATED)
                .message("Issue created successfully")
                .data(savedIssueDTO)
                .build();
    }

    @Transactional
    @PutMapping("/{id}")
    public Response<?> updateIssue(
            @PathVariable("id") Long id,
            @RequestBody IssueDTO issueDTO
    ){

        return null;
    }

    @DeleteMapping("/{id}")
    public Response<?> removeIssue(@PathVariable("id") Long id){
        Issue removeIssue = issueService.findById(id);
        removeIssue.setFlag(false);
        issueService.update(removeIssue);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issue removed successfully")
                .data(null)
                .build();
    }

    @GetMapping("/{id}")
    public Response<?> getIssueDetails(@PathVariable("id") Long id){

        Issue findIssue = issueService.findById(id);

        if (findIssue == null){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Issue not found")
                    .data(null)
                    .build();
        } else {
            if (!findIssue.getFlag()){
                return Response.builder()
                        .status(HttpStatus.NO_CONTENT)
                        .message("Issue doesn't exist")
                        .data(null)
                        .build();
            }

            IssueDTO issueDTO = IssueDTO.builder()
                    .title(findIssue.getTitle())
                    .status(findIssue.getStatus())
                    .description(findIssue.getDescription())
                    .postedBy(findIssue.getPostedBy().getId())
                    .trackingNumber(findIssue.getTrackingNumber())
                    .postedOn(findIssue.getCreateDate())
                    .build();

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("Issue found")
                    .data(issueDTO)
                    .build();
        }
    }
}
