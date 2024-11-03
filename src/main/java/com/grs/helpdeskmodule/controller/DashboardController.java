package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.service.DashboardService;
import com.grs.helpdeskmodule.utils.IssueMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/count")
    public Response<?> getCount(){
        Map<String,Integer> countList = new HashMap<>();
        Integer OPEN = dashboardService.findIssuesByStatus("OPEN").size();
        countList.put("open", OPEN);
        Integer RESOLVED = dashboardService.findIssuesByStatus("RESOLVED").size();
        countList.put("resolved", RESOLVED);
        Integer REJECTED = dashboardService.findIssuesByStatus("REJECTED").size();
        countList.put("rejected", REJECTED);
        Integer ONGOING = dashboardService.findIssuesByStatus("ONGOING").size();
        countList.put("ongoing", ONGOING);
        Integer CLOSED = dashboardService.findIssuesByStatus("CLOSED").size();
        countList.put("closed", CLOSED);

        Integer totalCount = OPEN+REJECTED+RESOLVED+ONGOING+CLOSED;
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

    @PostMapping("/status")
    public Response<?> findIssuesByStatus(
            @RequestParam(value = "status", required = false) String status
    ){
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

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All "+status+" issues found")
                .data(withAttachments(issues))
                .build();
    }

    @PostMapping("/current_month")
    public Response<?> findIssuesByYearMonth(
            @RequestParam(value = "YearMonth", required = false) String YearMonth
    ){

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

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All issues on "+(YearMonth == null ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : YearMonth)+" found")
                .data(withAttachments(issues))
                .build();
    }

    @PostMapping("/between_month")
    public Response<?> findIssuesBetweenYearMonths(
            @RequestParam(value = "startYearMonth", required = false) String startYearMonth,
            @RequestParam(value = "endYearMonth",required = false) String endYearMonth
    ){

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

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All issues found between "+startYearMonth+" and "+endYearMonth)
                .data(withAttachments(issues))
                .build();
    }
    @PostMapping("/tracking")
    public Response<?> findIssuesByTrackingNumber(
            @RequestParam(value = "trackingNumber", required = false) String trackingNumber
    ){

        if (trackingNumber == null){
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("trackingNumber parameter missing")
                    .data(null)
                    .build();
        }

        Issue issue = dashboardService.findIssuesByTrackingNumber(trackingNumber);

        if (issue == null){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No issues found with tracking number "+trackingNumber)
                    .data(null)
                    .build();
        }

        Map<Long,String> filenames = new HashMap<>();
        for (Attachment a : issue.getAttachments()){
            filenames.put(a.getId(),a.getFileName());
        }
        IssueDTO issueDTO = IssueMapper.convertToIssueDTO(issue);
        issueDTO.setAttachments(filenames);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Issues found with tracking number "+trackingNumber)
                .data(issueDTO)
                .build();
    }
    @PostMapping("/find")
    public Response<?> findIssuesByTitleOrDescription(
            @RequestParam(value = "input", required = false) String input
    ){

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

                    Map<Long,String> filenames = new HashMap<>();

                    for (Attachment a : issue.getAttachments()){
                        filenames.put(a.getId(),a.getFileName());
                    }

                    IssueDTO convertedIssue = IssueMapper.convertToIssueDTO(issue);
                    convertedIssue.setAttachments(filenames);

                    return convertedIssue;
                }
        ).toList();
    }
}
