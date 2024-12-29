package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.entity.Issue;
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

    @Query(value = "SELECT * FROM helpdesk.issue WHERE tracking_number = :tracking_number", nativeQuery = true)
    Issue findIssuesByTrackingNumber(String tracking_number);

    @Query(value = "SELECT * FROM helpdesk.issue WHERE title LIKE CONCAT('%', :input, '%') OR description LIKE CONCAT('%', :input, '%')", nativeQuery = true)
    List<Issue> findIssuesByTitleOrDescription(@Param("input") String input);

    @Query(value = "SELECT * FROM helpdesk.issue WHERE user_id = :user_id", nativeQuery = true)
    List<Issue> findIssuesByUser(@Param("user_id") Long user_id);
}
