package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserInformation;
import com.grs.helpdeskmodule.entity.*;
import com.grs.helpdeskmodule.service.*;
import com.grs.helpdeskmodule.utils.AttachmentUtils;
import com.grs.helpdeskmodule.utils.IssueUtils;
import com.grs.helpdeskmodule.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final UserService userService;
    private final AttachmentService attachmentService;
    private final OfficeService officeService;
    private final DashboardService dashboardService;
    private final UserUtils userUtils;
    private final EmailService emailService;

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
        User postedByUser = userService.findUserByUsername(loggedInUserEmail);

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

        List<Map<String, Object>> transformedAttachments = AttachmentUtils.getAttachmentFilenames(savedIssue);

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
                .attachments(transformedAttachments)
                .build();

//        emailService.sendEmail(MailBody.builder()
//                        .to(savedIssue.getPostedBy().getEmail())
//                        .subject("ইসু সফলভাবে জমাদানকৃত")
//                        .body(MailBody.sendIssueSubmittedEmail(savedIssue.getPostedBy().getName(), savedIssue.getTrackingNumber()))
//                .build());

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

        List<Map<String, Object>> transformedAttachments = new ArrayList<>();

        if (attachments != null && !attachments.isEmpty()) {
            for (Attachment a : updatedIssue.getAttachments()) {
                Map<String, Object> attachmentData = new HashMap<>();
                attachmentData.put("fileName", a.getFileName());
                attachmentData.put("id", a.getId());
                transformedAttachments.add(attachmentData);
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
                                .attachments(transformedAttachments)
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

            List<Map<String, Object>> transformedAttachments = AttachmentUtils.getAttachmentFilenames(findIssue);

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
                    .attachments(transformedAttachments)
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
    public Response<?> getIssuesByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User postedByUser = userService.findUserByUsername(loggedInUserEmail);

        Page<Issue> issueList = issueService.findIssueByUser(postedByUser.getId(), page, size);

        if (issueList.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found for this user")
                    .data(null)
                    .build();
        }

        List<IssueDTO> issueDTOList = IssueUtils.responseIssueListDTO(issueList);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues for user id "+postedByUser.getId()+" has been retrieved")
                .data(issueDTOList)
                .build();
    }

    @GetMapping("/inbox")
    public Response<?> getIssuesByOffice(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){

        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User postedByUser = userService.findUserByUsername(loggedInUserEmail);
        Long officeId = postedByUser.getOffice().getId();

        Page<Issue> issueList = issueService.findIssueByOffice(officeId, page, size);

        if (issueList.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found for this office")
                    .data(null)
                    .build();
        }

        List<IssueDTO> issueDTOList = IssueUtils.responseIssueListDTO(issueList);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues for office id "+postedByUser.getOffice().getId()+" has been retrieved")
                .data(issueDTOList)
                .build();
    }

    /**
     * Finds issues by a specified status. If the status is invalid or missing, returns a bad request response.
     * Handles HTTP POST requests to the "/status" endpoint.
     *
     * @param status The status of the issues to retrieve (e.g., "OPEN", "RESOLVED").
     * @return A response containing a list of issues with the specified status or a message if no issues are found.
     */

    @PostMapping("/{userType}/status/{status}")
    public Response<?> findIssuesByStatus(
            @PathVariable String status,
            @PathVariable Long userType,
            Authentication authentication
    ){
        UserInformation user = userUtils.extractUserInformation(authentication);
        Long userId = user.getId();
        Long officeId = user.getOfficeId();

        if (status == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Status parameter missing")
                    .data(null)
                    .build();
        }
        status = status.toUpperCase();
        try {
            IssueStatus.valueOf(status);
        } catch (Exception e){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid status")
                    .data(null)
                    .build();
        }

        List<Issue> issues = dashboardService.findIssuesByStatus(status);

        if (issues.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No "+status+" issues found")
                    .data(null)
                    .build();
        }

        if(userType == 0L){
            issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
        } else {
            issues = issues.stream().filter(i -> Objects.equals(i.getPostedBy().getId(), userId)).toList();
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All "+status+" issues found")
                .data(withAttachments(issues))
                .build();
    }

    /**
     * Finds issues based on a specified year and month. If no parameter is provided, defaults to the current month.
     * Handles HTTP POST requests to the "/current_month" endpoint.
     *
     * @param YearMonth The year and month (in "yyyy-MM" format) to filter issues by. Optional.
     * @return A response containing a list of issues from the specified month or a message if no issues are found.
     */

    @PostMapping("/{userType}/current_month/{YearMonth}")
    public Response<?> findIssuesByYearMonth(
            @PathVariable String YearMonth,
            @PathVariable Long userType,
            Authentication authentication
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();
        Long userId = user.getId();

        List<Issue> issues;

        if (YearMonth == null){
            issues = dashboardService.findIssuesByYearMonth();
        } else {
            issues = dashboardService.findIssuesByYearMonth(YearMonth);
        }

        if (issues.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues on "+YearMonth+" found")
                    .data(null)
                    .build();
        }

        if(userType == 0L){
            issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
        } else {
            issues = issues.stream().filter(i -> Objects.equals(i.getPostedBy().getId(), userId)).toList();
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All issues on "+(YearMonth == null ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : YearMonth)+" found")
                .data(withAttachments(issues))
                .build();
    }

    /**
     * Finds issues between two specified year-months (inclusive).
     * Both parameters are required; returns a bad request response if either is missing.
     * Handles HTTP POST requests to the "/between_month" endpoint.
     *
     * @param startYearMonth The start year and month (in "yyyy-MM" format) for filtering issues.
     * @param endYearMonth   The end year and month (in "yyyy-MM" format) for filtering issues.
     * @return A response containing a list of issues found within the specified date range, or a message if no issues are found.
     */

    @PostMapping("/{userType}/between_month/{startYearMonth}/{endYearMonth}")
    public Response<?> findIssuesBetweenYearMonths(
            @PathVariable String startYearMonth,
            @PathVariable String endYearMonth,
            @PathVariable Long userType,
            Authentication authentication
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();
        Long userId = user.getId();

        if (startYearMonth == null || endYearMonth == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("startYearMonth or endYearMonth parameter missing")
                    .data(null)
                    .build();
        }

        List<Issue> issues = issueService.findIssuesBetweenYearMonths(startYearMonth,endYearMonth);

        if (issues.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found between "+startYearMonth+" and "+endYearMonth)
                    .data(null)
                    .build();
        }

        if(userType == 0L){
            issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
        } else {
            issues = issues.stream().filter(i -> Objects.equals(i.getPostedBy().getId(), userId)).toList();
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All issues found between "+startYearMonth+" and "+endYearMonth)
                .data(withAttachments(issues))
                .build();
    }

    /**
     * Finds an issue based on a specified tracking number. Returns a message if no issue is found with the provided tracking number.
     * Handles HTTP POST requests to the "/tracking" endpoint.
     *
     * @param trackingNumber The tracking number of the issue to retrieve.
     * @return A response containing the issue details, including attachments, or a message if the issue is not found.
     */

    @PostMapping("/{userType}/tracking/{trackingNumber}")
    public Response<?> findIssuesByTrackingNumber(
            @PathVariable String trackingNumber,
            @PathVariable Long userType,
            Authentication authentication
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long userId = user.getId();
        Long officeId = user.getOfficeId();

        if (trackingNumber == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("trackingNumber parameter missing")
                    .data(null)
                    .build();
        }

        List<Issue> issues = issueService.findByTrackingNumber(trackingNumber);
        if(userType == 0L){
            issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
        } else {
            issues = issues.stream().filter(i -> Objects.equals(i.getPostedBy().getId(), userId)).toList();
        }
        List<IssueDTO> issueDTOList = IssueUtils.convertToIssueDTOList(issues);

        if (issueDTOList.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found with tracking number "+trackingNumber)
                    .data(null)
                    .build();
        }

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues found with tracking number "+trackingNumber)
                .data(issueDTOList)
                .build();
    }

    /**
     * Finds issues that contain a specified text in their title or description.
     * Handles HTTP POST requests to the "/find" endpoint.
     *
     * @param input The text to search for in issue titles or descriptions.
     * @return A response containing a list of issues that match the specified text, or a message if no matches are found.
     */

    @PostMapping("/{userType}/find/{input}")
    public Response<?> findIssuesByTitleOrDescription(
            @PathVariable String input,
            @PathVariable Long userType,
            Authentication authentication
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long userId = user.getId();
        Long officeId = user.getOfficeId();

        if (input == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Input parameter missing")
                    .data(null)
                    .build();
        }

        List<Issue> issues = issueService.findIssuesByTitleOrDescription(input);

        if (issues.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found with with the text "+input)
                    .data(null)
                    .build();
        }

        if(userType == 0L){
            issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
        } else {
            issues = issues.stream().filter(i -> Objects.equals(i.getPostedBy().getId(), userId)).toList();
        }

        List<IssueDTO> issueDTOList = withAttachments(issues);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues found with the text "+input)
                .data(issueDTOList)
                .build();
    }

    private List<IssueDTO> withAttachments(List<Issue> issues) {
        return issues.stream().map(issue -> {
            List<Map<String, Object>> filenames = AttachmentUtils.getAttachmentFilenames(issue);
            IssueDTO convertedIssue = IssueUtils.convertToIssueDTO(issue);
            convertedIssue.setAttachments(filenames);
            return convertedIssue;
        }).toList();
    }
}
