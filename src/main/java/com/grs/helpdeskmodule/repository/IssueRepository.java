package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueRepository extends BaseEntityRepository<Issue> {

    @Query(value = "SELECT * FROM helpdesk.issue WHERE status = :status", nativeQuery = true)
    List<Issue> findIssuesByStatus(@Param("status") String status);

    @Query(value = "SELECT * FROM helpdesk.issue WHERE DATE_FORMAT(created_at, '%Y-%m') = DATE_FORMAT(:yearMonth, '%Y-%m')", nativeQuery = true)
    List<Issue> findIssuesByYearMonth(@Param("yearMonth") String yearMonth);

    @Query(value = "SELECT * FROM helpdesk.issue WHERE DATE_FORMAT(created_at, '%Y-%m') BETWEEN DATE_FORMAT(:startYearMonth, '%Y-%m') AND DATE_FORMAT(:endYearMonth, '%Y-%m')", nativeQuery = true)
    List<Issue> findIssuesBetweenYearMonths(@Param("startYearMonth") String startYearMonth, @Param("endYearMonth") String endYearMonth);

    @Query(value = "SELECT * FROM helpdesk.issue WHERE tracking_number = :trx", nativeQuery = true)
    Issue findIssuesByTrackingNumber(@Param("trx") String trackingNumber);

    @Query(value = "SELECT * FROM helpdesk.issue WHERE title LIKE CONCAT('%', :input, '%') OR description LIKE CONCAT('%', :input, '%')", nativeQuery = true)
    List<Issue> findIssuesByTitleOrDescription(@Param("input") String input);
}
