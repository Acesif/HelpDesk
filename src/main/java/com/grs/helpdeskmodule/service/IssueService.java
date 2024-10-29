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

    public IssueService(BaseEntityRepository<Issue> baseRepository, IssueRepository issueRepository) {
        super(baseRepository);
    }
}
