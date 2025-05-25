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
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Response<?> getCount(Authentication authentication) {

        UserInformation user = userUtils.extractUserInformation(authentication);
        Long officeId = user.getOfficeId();

        Map<String,Integer> countList = new HashMap<>();
        List<Issue> OPEN = dashboardService.findIssuesByStatus("OPEN");
        List<Issue> RESOLVED = dashboardService.findIssuesByStatus("RESOLVED");
        List<Issue> REJECTED = dashboardService.findIssuesByStatus("REJECTED");
        List<Issue> ONGOING = dashboardService.findIssuesByStatus("ONGOING");
        List<Issue> CLOSED = dashboardService.findIssuesByStatus("CLOSED");

        if (officeId != null) {
            OPEN = OPEN.stream().filter(i -> Objects.equals(i.getOfficeId(), officeId)).toList();
            RESOLVED = RESOLVED.stream().filter(i -> Objects.equals(i.getOfficeId(), officeId)).toList();
            REJECTED = REJECTED.stream().filter(i -> Objects.equals(i.getOfficeId(), officeId)).toList();
            ONGOING = ONGOING.stream().filter(i -> Objects.equals(i.getOfficeId(), officeId)).toList();
            CLOSED = CLOSED.stream().filter(i -> Objects.equals(i.getOfficeId(), officeId)).toList();
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
}
