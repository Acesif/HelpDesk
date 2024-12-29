package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.base.BaseEntity;
import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.*;
import com.grs.helpdeskmodule.repository.OfficeRepository;
import com.grs.helpdeskmodule.service.AttachmentService;
import com.grs.helpdeskmodule.service.IssueService;
import com.grs.helpdeskmodule.service.OfficeService;
import com.grs.helpdeskmodule.service.UserService;
import com.grs.helpdeskmodule.utils.AttachmentUtils;
import com.grs.helpdeskmodule.utils.IssueUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;
    private final AttachmentService attachmentService;
    private final OfficeService officeService;

    /**
     * Creates a new issue with the given title, description, and status. Optionally allows attaching files.
     * Ensures that the operation is atomic and rolls back in case of any failure.
     * Handles HTTP POST requests to the "/new" endpoint, consuming multipart form data.
     *
     * @param title The title of the issue.
     * @param description The description of the issue.
     * @param attachments Optional list of files to be attached to the issue.
     *
     * @return A generic response indicating the outcome of the issue creation.
     */

    @Transactional
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> createIssue(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("officeId") Long officeId,
            @RequestParam(value = "attachments", required = false) List<MultipartFile> attachments)
    {

        IssueDTO issueDto = IssueDTO.builder()
                .title(title)
                .description(description)
                .status(IssueStatus.OPEN)
                .category(IssueCategory.valueOf(category.toUpperCase()))
                .build();

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User postedByUser = userService.findUserByEmail(loggedInUserEmail);

        Issue issue = Issue.builder()
                .flag(true)
                .title(issueDto.getTitle())
                .description(issueDto.getDescription())
                .status(issueDto.getStatus())
                .office(officeService.getOffice(officeId))
                .trackingNumber(AttachmentUtils.generateTrackingNumber())
                .issueCategory(IssueCategory.valueOf(category.toUpperCase()))
                .postedBy(postedByUser)
                .build();


        AttachmentUtils.convertMultipartToAttachmentToSave(attachments,issue);

        Issue savedIssue = issueService.save(issue);

        Map<Long,String> filenames = new HashMap<>();

        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment a : savedIssue.getAttachments()){
                filenames.put(a.getId(),a.getFileName());
            }
        }

        IssueDTO savedIssueDTO = IssueDTO.builder()
                .id(savedIssue.getId())
                .title(savedIssue.getTitle())
                .description(savedIssue.getDescription())
                .status(savedIssue.getStatus())
                .trackingNumber(savedIssue.getTrackingNumber())
                .postedOn(savedIssue.getCreateDate())
                .updatedOn(savedIssue.getUpdateDate())
                .officeId(savedIssue.getOffice() != null ? savedIssue.getOffice().getId() : null)
                .postedBy(savedIssue.getPostedBy().getId())
                .category(savedIssue.getIssueCategory())
                .attachments(filenames)
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
            @RequestPart("category") String category,
            @RequestPart("officeId") String officeId,
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
        issue.setOffice(officeService.getOffice(Long.parseLong(officeId)));
        issue.setIssueCategory(IssueCategory.valueOf(category.toUpperCase()));
        issue.setUpdateDate(new Date());

        AttachmentUtils.convertMultipartToAttachmentToUpdate(attachments,issue);

        Issue updatedIssue = issueService.update(issue);

        Map<Long,String> filenames = new HashMap<>();

        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment a : updatedIssue.getAttachments()){
                filenames.put(a.getId(),a.getFileName());
            }
        }

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
                                .officeId(updatedIssue.getOffice() != null ? updatedIssue.getOffice().getId() : null)
                                .updatedOn(updatedIssue.getUpdateDate())
                                .trackingNumber(updatedIssue.getTrackingNumber())
                                .category(updatedIssue.getIssueCategory())
                                .attachments(filenames)
                                .build()
                )
                .build();
    }

    /**
     * Soft deletes an issue by setting its flag to false. This method does not permanently delete the issue
     * from the database, but removes it from active status. Also removes associated attachments.
     * Handles HTTP DELETE requests to the "/{id}" endpoint.
     *
     * @param id The ID of the issue to be removed.
     * @return A generic response indicating the outcome of the issue removal.
     */
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

    /**
     * Retrieves the details of an issue by its ID. If the issue is inactive (flag set to false),
     * returns a message indicating the issue doesn't exist.
     * Handles HTTP GET requests to the "/{id}" endpoint.
     *
     * @param id The ID of the issue to retrieve.
     * @return A response containing the issue details or an error message if not found.
     */
    @GetMapping("/{id}")
    public Response<?> getIssueDetails(@PathVariable("id") String id){

        Issue findIssue = issueService.findByTrackingNumber(id);

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

            Map<Long,String> filenames = new HashMap<>();

            if (findIssue.getAttachments() != null && !findIssue.getAttachments().isEmpty()){
                for (Attachment a : findIssue.getAttachments()){
                    filenames.put(a.getId(),a.getFileName());
                }
            }

            IssueDTO issueDTO = IssueDTO.builder()
                    .id(findIssue.getId())
                    .title(findIssue.getTitle())
                    .status(findIssue.getStatus())
                    .description(findIssue.getDescription())
                    .postedBy(findIssue.getPostedBy().getId())
                    .postedOn(findIssue.getCreateDate())
                    .updatedOn(findIssue.getUpdateDate())
                    .officeId(findIssue.getOffice() != null ? findIssue.getOffice().getId() : null)
                    .trackingNumber(findIssue.getTrackingNumber())
                    .category(findIssue.getIssueCategory())
                    .attachments(filenames)
                    .build();

            return Response.builder()
                    .status(HttpStatus.OK)
                    .message("Issue found")
                    .data(issueDTO)
                    .build();
        }
    }

    /**
     * Retrieves a list of issues created by a specific user, sorted by update date.
     * Handles HTTP GET requests to the "/user/{id}" endpoint.
     *
     * @return A list of issues for the specified user, or a message indicating no issues found.
     */
    @GetMapping("/user")
    public Response<?> getIssuesByUser(){

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User postedByUser = userService.findUserByEmail(loggedInUserEmail);

        List<Issue> issueList = issueService.findIssueByUser(postedByUser.getId());

        if (issueList.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found for this user")
                    .data(null)
                    .build();
        }

        List<Issue> sortedIssueDTOList = issueList.stream().sorted(Comparator.comparing(BaseEntity::getUpdateDate)).toList();

        List<IssueDTO> issueDTOList = sortedIssueDTOList.stream().map(IssueUtils::convertToIssueDTO).toList();

        Map<Long, Issue> issueMap = issueList.stream()
                .collect(Collectors.toMap(Issue::getId, issue -> issue));

        for (IssueDTO issueDTO : issueDTOList) {
            Issue correspondingIssue = issueMap.get(issueDTO.getId());
            if (correspondingIssue != null) {
                Map<Long, String> filenames = new HashMap<>();
                if (correspondingIssue.getAttachments() != null && !correspondingIssue.getAttachments().isEmpty()){
                    for (Attachment attachment : correspondingIssue.getAttachments()) {
                        filenames.put(attachment.getId(), attachment.getFileName());
                    }
                }
                issueDTO.setAttachments(filenames);
            }
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues for user id "+postedByUser.getId()+" has been retrieved")
                .data(issueDTOList)
                .build();
    }
}
