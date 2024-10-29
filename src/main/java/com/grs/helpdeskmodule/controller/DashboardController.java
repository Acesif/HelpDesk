package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.service.DashboardService;
import com.grs.helpdeskmodule.utils.IssueMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * findIssuesByStatus(IssueStatus status)
     * findIssuesByYearMonth(String YearMonth)
     * findIssuesByYearMonth()
     * findIssuesBetweenYearMonths(String startYearMonth, String endYearMonth)
     * findIssuesByTrackingNumber(String trackingNumber)
     * findIssuesByTitleOrDescription(String input)
     */

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

        List<IssueDTO> issueDTOList = issues.stream().map(IssueMapper::convertToIssueDTO).toList();

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All "+status+" issues found")
                .data(issueDTOList)
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

        List<IssueDTO> issueDTOList = issues.stream().map(IssueMapper::convertToIssueDTO).toList();

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All issues on "+(YearMonth == null ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : YearMonth)+" found")
                .data(issueDTOList)
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

        List<IssueDTO> issueDTOList = issues.stream().map(IssueMapper::convertToIssueDTO).toList();

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All issues found between "+startYearMonth+" and "+endYearMonth)
                .data(issueDTOList)
                .build();
    }
    @PostMapping("/tracking")
    public Response<?> findIssuesByTrackingNumber(
            @RequestParam(value = "trackingNumber", required = false) String trackingNumber
    ){

        return Response.builder()
                .status(HttpStatus.OK)
                .message("")
                .data(null)
                .build();
    }
    @PostMapping("/input")
    public Response<?> findIssuesByTitleOrDescription(
            @RequestParam(value = "input", required = false) String input
    ){

        return Response.builder()
                .status(HttpStatus.OK)
                .message("")
                .data(null)
                .build();
    }
}
