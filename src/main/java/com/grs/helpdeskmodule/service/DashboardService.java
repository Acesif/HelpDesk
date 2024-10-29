package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.repository.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DashboardService extends BaseService<Issue> {

    private final IssueRepository issueRepository;

    public DashboardService(BaseEntityRepository<Issue> baseRepository, IssueRepository issueRepository) {
        super(baseRepository);
        this.issueRepository = issueRepository;
    }

    public List<Issue> findIssuesByStatus(String status){
        return issueRepository.findIssuesByStatus(status);
    }

    public List<Issue> findIssuesByYearMonth(String YearMonth){
        return issueRepository.findIssuesByYearMonth(YearMonth);
    }
    public List<Issue> findIssuesByYearMonth(){
        return issueRepository.findIssuesByYearMonth(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    public List<Issue> findIssuesBetweenYearMonths(String startYearMonth, String endYearMonth){
        return issueRepository.findIssuesBetweenYearMonths(startYearMonth,endYearMonth);
    }

    public Issue findIssuesByTrackingNumber(String trackingNumber){
        return issueRepository.findIssuesByTrackingNumber(trackingNumber);
    }

    public List<Issue> findIssuesByTitleOrDescription(String input){
        return issueRepository.findIssuesByTitleOrDescription(input);
    }
}
