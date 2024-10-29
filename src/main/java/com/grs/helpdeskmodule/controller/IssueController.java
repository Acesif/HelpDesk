package com.grs.helpdeskmodule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.repository.AttachmentRepository;
import com.grs.helpdeskmodule.service.AttachmentService;
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
import java.util.Date;
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
    private final AttachmentService attachmentService;

    /**
     * Creates a new issue with the given title, description, and status. Optionally allows attaching files.
     * Ensures that the operation is atomic and rolls back in case of any failure.
     * Handles HTTP POST requests to the "/new" endpoint, consuming multipart form data.
     *
     * @param title The title of the issue.
     * @param description The description of the issue.
     * @param status The status of the issue.
     * @param attachments Optional list of files to be attached to the issue.
     *
     * @return A generic response indicating the outcome of the issue creation.
     */

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


        IssueMapper.convertMultipartToAttachmentToSave(attachments,issue);

        Issue savedIssue = issueService.save(issue);

        IssueDTO savedIssueDTO = IssueDTO.builder()
                .id(savedIssue.getId())
                .title(savedIssue.getTitle())
                .description(savedIssue.getDescription())
                .status(savedIssue.getStatus())
                .trackingNumber(savedIssue.getTrackingNumber())
                .postedOn(savedIssue.getCreateDate())
                .updatedOn(savedIssue.getUpdateDate())
                .postedBy(savedIssue.getPostedBy().getId())
                .build();

        return Response.builder()
                .status(HttpStatus.CREATED)
                .message("Issue created successfully")
                .data(savedIssueDTO)
                .build();
    }

    /**
     * Updates an existing issue using their issue id and sending the updated params. Optionally allows attaching files.
     * Ensures that the operation is atomic and rolls back in case of any failure.
     * Handles HTTP PUT requests to the "/{id}" endpoint, consuming multipart form data.
     *
     * @param title The title of the issue.
     * @param description The description of the issue.
     * @param status The status of the issue.
     * @param attachments Optional list of files to be attached to the issue.
     *
     * @return A generic response indicating the outcome of the issue creation.
     */

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> updateIssue(
            @PathVariable("id") Long id,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("status") String  status,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ){
        Issue issue = issueService.findById(id);

        if (issue == null || !issue.getFlag()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Issue not found")
                    .data(null)
                    .build();
        }

        issue.setFlag(true);
        issue.setStatus(IssueStatus.valueOf(status));
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setUpdateDate(new Date());

        IssueMapper.convertMultipartToAttachmentToUpdate(attachments,issue);

        Issue updatedIssue = issueService.update(issue);


        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issue successfully updated")
                .data(
                        IssueDTO.builder()
                                .id(updatedIssue.getId())
                                .title(updatedIssue.getTitle())
                                .status(updatedIssue.getStatus())
                                .description(updatedIssue.getDescription())
                                .postedBy(updatedIssue.getPostedBy().getId())
                                .postedOn(updatedIssue.getCreateDate())
                                .updatedOn(updatedIssue.getUpdateDate())
                                .trackingNumber(updatedIssue.getTrackingNumber())
                                .build()
                )
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<?> removeIssue(@PathVariable("id") Long id){
        Issue removeIssue = issueService.findById(id);
        removeIssue.setFlag(false);
        issueService.update(removeIssue);
        attachmentService.removeAll(id);

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
                    .id(findIssue.getId())
                    .title(findIssue.getTitle())
                    .status(findIssue.getStatus())
                    .description(findIssue.getDescription())
                    .postedBy(findIssue.getPostedBy().getId())
                    .postedOn(findIssue.getCreateDate())
                    .updatedOn(findIssue.getUpdateDate())
                    .trackingNumber(findIssue.getTrackingNumber())
                    .build();

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("Issue found")
                    .data(issueDTO)
                    .build();
        }
    }
}
