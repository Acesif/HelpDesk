package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    public Issue findById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }

    public Issue save(Issue entity) {
        entity.setCreateDate(new Date());
        entity.setFlag(true);
        return issueRepository.saveAndFlush(entity);
    }

    public Issue update(Issue entity) {
        entity.setUpdateDate(new Date());
        return issueRepository.save(entity);
    }

    public void delete(Long id) {
        Issue entity = issueRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setFlag(false);
            update(entity);
        }
    }

    public void hardDelete(Long id) {
        issueRepository.deleteById(id);
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
