package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.repository.IssueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService extends BaseService<Issue> {

    private final IssueRepository issueRepository;

    public IssueService(BaseEntityRepository<Issue> baseRepository, IssueRepository issueRepository) {
        super(baseRepository);
        this.issueRepository = issueRepository;
    }

    public Page<Issue> findIssueByUser(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return issueRepository.findIssuesByUser(id, pageable);
    }

    public Page<Issue> findIssueByOffice(Long id, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return issueRepository.findIssuesByOffice(id, pageable);
    }

    public List<Issue> findByTrackingNumber(String trackingNumber){
        return issueRepository.findIssuesByTrackingNumber(trackingNumber);
    }

    public List<Issue> findIssuesBetweenYearMonths(String startYearMonth, String endYearMonth){
        return issueRepository.findIssuesBetweenYearMonths(startYearMonth, endYearMonth);
    }

    public List<Issue> findIssuesByTitleOrDescription(String input){
        return issueRepository.findIssuesByTitleOrDescription(input);
    }

    public Page<Issue> findAllIssues(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return issueRepository.findAll(pageable);
    }
}
