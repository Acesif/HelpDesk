package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public Attachment findById(Long id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    public Attachment save(Attachment entity) {
        entity.setCreateDate(new Date());
        entity.setFlag(true);
        return attachmentRepository.saveAndFlush(entity);
    }

    public Attachment update(Attachment entity) {
        entity.setUpdateDate(new Date());
        return attachmentRepository.save(entity);
    }

    public void delete(Long id) {
        Attachment entity = attachmentRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setFlag(false);
            update(entity);
        }
    }

    public void hardDelete(Long id) {
        attachmentRepository.deleteById(id);
    }

    public void removeAll(Long issue_id){
        attachmentRepository.removeBulkAttachments(issue_id);
    }

    public Set<Attachment> findAllByIssue(Long issue_id){
        return attachmentRepository.getAllByIssueId(issue_id);
    }
}
