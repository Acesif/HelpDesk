package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.entity.IssueReplies;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueReplyRepository extends BaseEntityRepository<IssueReplies> {

    @Query(value = "SELECT * FROM issue_replies WHERE parent_issue_id = :parentId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    IssueReplies findLastIssue(@Param("parentId") Long parentId);

}
