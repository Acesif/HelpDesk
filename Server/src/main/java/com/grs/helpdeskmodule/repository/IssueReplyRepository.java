package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.entity.IssueReplies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueReplyRepository extends JpaRepository<IssueReplies, Long> {

    @Query(value = "SELECT * FROM issue_replies WHERE parent_issue_id = :parentId ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    IssueReplies findLastIssue(@Param("parentId") Long parentId);

    @Query(value = "SELECT * FROM issue_replies WHERE parent_issue_id = :parentId", nativeQuery = true)
    List<IssueReplies> findIssueRepliesByIssueId(@Param("parentId") Long parentId);

}