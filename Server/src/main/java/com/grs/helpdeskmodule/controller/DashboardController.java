package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.dto.UserInformation;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.service.DashboardService;
import com.grs.helpdeskmodule.utils.IssueUtils;
import com.grs.helpdeskmodule.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserUtils userUtils;

    /**
     * Retrieves the count of issues grouped by status and other statistical metrics, including
     * issues from the previous and current month.
     * Handles HTTP GET requests to the "/count" endpoint.
     *
     * @return A response containing a map with the count of issues by status, total count,
     *         previous month's issues, and current month's issues.
     */
    @GetMapping("/count")
    public Response<?> getCount(Authentication authentication){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();

        Map<String,Integer> countList = new HashMap<>();
        List<Issue> OPEN = dashboardService.findIssuesByStatus("OPEN");
        List<Issue> RESOLVED = dashboardService.findIssuesByStatus("RESOLVED");
        List<Issue> REJECTED = dashboardService.findIssuesByStatus("REJECTED");
        List<Issue> ONGOING = dashboardService.findIssuesByStatus("ONGOING");
        List<Issue> CLOSED = dashboardService.findIssuesByStatus("CLOSED");

        if (officeId != null) {
            OPEN = OPEN.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
            RESOLVED = RESOLVED.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
            REJECTED = REJECTED.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
            ONGOING = ONGOING.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
            CLOSED = CLOSED.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();
        }

        countList.put("open", OPEN.size());
        countList.put("resolved", RESOLVED.size());
        countList.put("rejected", REJECTED.size());
        countList.put("ongoing", ONGOING.size());
        countList.put("closed", CLOSED.size());

        Integer totalCount = OPEN.size() + REJECTED.size() + RESOLVED.size() + ONGOING.size() + CLOSED.size();
        countList.put("total", totalCount);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        Date previousMonthDate = calendar.getTime();

        Integer prevMonthIssues = dashboardService.findIssuesByYearMonth(dateFormat.format(previousMonthDate)).size();
        Integer currentMonthIssues = dashboardService.findIssuesByYearMonth().size();

        countList.put("time_exceeded", prevMonthIssues);
        countList.put("current_month_issues", currentMonthIssues);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Statistics of issues")
                .data(countList)
                .build();
    }

    /**
     * Finds issues by a specified status. If the status is invalid or missing, returns a bad request response.
     * Handles HTTP POST requests to the "/status" endpoint.
     *
     * @param status The status of the issues to retrieve (e.g., "OPEN", "RESOLVED").
     * @return A response containing a list of issues with the specified status or a message if no issues are found.
     */
    @PostMapping("/status")
    public Response<?> findIssuesByStatus(
            Authentication authentication,
            @RequestParam(value = "status", required = false) String status
    ){
        UserInformation user = userUtils.extractUserInformation(authentication);
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

        issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();

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
    @PostMapping("/current_month")
    public Response<?> findIssuesByYearMonth(
            Authentication authentication,
            @RequestParam(value = "YearMonth", required = false) String YearMonth
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();

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

        issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();

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
    @PostMapping("/between_month")
    public Response<?> findIssuesBetweenYearMonths(
            Authentication authentication,
            @RequestParam(value = "startYearMonth", required = false) String startYearMonth,
            @RequestParam(value = "endYearMonth",required = false) String endYearMonth
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();

        if (startYearMonth == null || endYearMonth == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("startYearMonth or endYearMonth parameter missing")
                    .data(null)
                    .build();
        }

        List<Issue> issues = dashboardService.findIssuesBetweenYearMonths(startYearMonth,endYearMonth);

        if (issues.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found between "+startYearMonth+" and "+endYearMonth)
                    .data(null)
                    .build();
        }

        issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();

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
    @PostMapping("/tracking")
    public Response<?> findIssuesByTrackingNumber(
            Authentication authentication,
            @RequestParam(value = "trackingNumber", required = false) String trackingNumber
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();

        if (trackingNumber == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("trackingNumber parameter missing")
                    .data(null)
                    .build();
        }

        Issue issue = dashboardService.findIssuesByTrackingNumber(trackingNumber);
        if (issue.getOffice().getId().equals(officeId)){
            issue = null;
        }

        if (issue == null){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found with tracking number "+trackingNumber)
                    .data(null)
                    .build();
        }

        Map<Long,String> filenames = getAttachmentFilenames(issue);
        IssueDTO issueDTO = IssueUtils.convertToIssueDTO(issue);
        issueDTO.setAttachments(filenames);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues found with tracking number "+trackingNumber)
                .data(issueDTO)
                .build();
    }

    /**
     * Finds issues that contain a specified text in their title or description.
     * Handles HTTP POST requests to the "/find" endpoint.
     *
     * @param input The text to search for in issue titles or descriptions.
     * @return A response containing a list of issues that match the specified text, or a message if no matches are found.
     */
    @PostMapping("/find")
    public Response<?> findIssuesByTitleOrDescription(
            Authentication authentication,
            @RequestParam(value = "input", required = false) String input
    ){

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();

        if (input == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Input parameter missing")
                    .data(null)
                    .build();
        }

        List<Issue> issues = dashboardService.findIssuesByTitleOrDescription(input);

        if (issues.isEmpty()){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found with with the text "+input)
                    .data(null)
                    .build();
        }

        issues = issues.stream().filter(i -> Objects.equals(i.getOffice().getId(), officeId)).toList();

        List<IssueDTO> issueDTOList = withAttachments(issues);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues found with the text "+input)
                .data(issueDTOList)
                .build();
    }

    private List<IssueDTO> withAttachments(List<Issue> issues) {
        return issues.stream().map(
                issue -> {

                    Map<Long,String> filenames = getAttachmentFilenames(issue);

                    IssueDTO convertedIssue = IssueUtils.convertToIssueDTO(issue);
                    convertedIssue.setAttachments(filenames);

                    return convertedIssue;
                }
        ).toList();
    }

    private Map<Long, String> getAttachmentFilenames(Issue issue){
        Map<Long,String> filenames = new HashMap<>();
        for (Attachment a : issue.getAttachments()){
            filenames.put(a.getId(),a.getFileName());
        }
        return filenames;
    }
}
