package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import com.grs.helpdeskmodule.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class IssueService extends BaseService<Issue> {

    private final IssueRepository issueRepository;
    public IssueService(BaseEntityRepository<Issue> baseRepository, IssueRepository issueRepository) {
        super(baseRepository);
        this.issueRepository = issueRepository;
    }

    public List<Issue> findIssueByUser(Long id){
        return issueRepository.findIssuesByUser(id);
    }

    public List<Issue> findIssueByOffice(Long id){
        return issueRepository.findIssuesByOffice(id);
    }

    public List<Issue> findByTrackingNumber(String trackingNumber){
        return issueRepository.findIssuesByTrackingNumber(trackingNumber);
    }

    public List<Issue> findIssuesBetweenYearMonths(String startYearMonth, String endYearMonth){
        return issueRepository.findIssuesBetweenYearMonths(startYearMonth,endYearMonth);
    }

    public List<Issue> findIssuesByTitleOrDescription(String input){
        return issueRepository.findIssuesByTitleOrDescription(input);
    }
}
