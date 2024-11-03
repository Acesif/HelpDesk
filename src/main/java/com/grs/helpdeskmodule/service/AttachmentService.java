package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AttachmentService extends BaseService<Attachment> {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(BaseEntityRepository<Attachment> baseRepository, AttachmentRepository attachmentRepository) {
        super(baseRepository);
        this.attachmentRepository = attachmentRepository;
    }

    public void removeAll(Long issue_id){
        attachmentRepository.removeBulkAttachments(issue_id);
    }

    public Set<Attachment> findAllByIssue(Long issue_id){
        return attachmentRepository.getAllByIssueId(issue_id);
    }
}
