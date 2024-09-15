package com.grs.helpdeskmodule.entity.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Issue;
import org.springframework.stereotype.Service;

@Service
public class IssueService extends BaseService<Issue> {
    public IssueService(BaseEntityRepository<Issue> baseRepository) {
        super(baseRepository);
    }
}
