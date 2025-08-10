package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

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

    public List<Issue> findIssuesByStatus(String status){
        return issueRepository.findIssuesByStatus(status);
    }

    public List<Issue> findIssuesByYearMonth(String YearMonth){
        return issueRepository.findIssuesByYearMonth(YearMonth);
    }
    public List<Issue> findIssuesByYearMonth(){
        return issueRepository.findIssuesByYearMonth(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}
