package com.grs.helpdeskmodule.repository;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.entity.Attachment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AttachmentRepository extends BaseEntityRepository<Attachment> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE helpdesk.attachments SET flag = false WHERE issue_id IN (:issue_id)", nativeQuery = true)
    void removeBulkAttachments(@Param("issue_id") Long issue_id);

    @Query(value = "SELECT * FROM helpdesk.attachments WHERE issue_id = :issue_id", nativeQuery = true)
    Set<Attachment> getAllByIssueId(@Param("issue_id") Long issue_id);
}
