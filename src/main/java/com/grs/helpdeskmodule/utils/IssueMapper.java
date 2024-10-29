package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.dto.AttachmentDTO;
import com.grs.helpdeskmodule.dto.IssueDTO;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IssueMapper {

    public static Set<AttachmentDTO> convertToAttachmentDTOs(Set<Attachment> attachments) {
        return attachments.stream().map(attachment -> {
            AttachmentDTO dto = new AttachmentDTO();
            dto.setFileName(attachment.getFileName());
            dto.setFileData(Base64.getEncoder().encodeToString(attachment.getFileData()));
            return dto;
        }).collect(Collectors.toSet());
    }

    public static Set<Attachment> convertToAttachments(Set<AttachmentDTO> attachmentDTOs) {
        return attachmentDTOs.stream().map(dto -> {
            Attachment attachment = new Attachment();
            attachment.setFileName(dto.getFileName());
            attachment.setFileData(Base64.getDecoder().decode(dto.getFileData()));
            return attachment;
        }).collect(Collectors.toSet());
    }

    public static IssueDTO convertToIssueDTO(Issue issue){
        return IssueDTO.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .status(issue.getStatus())
                .postedOn(issue.getCreateDate())
                .updatedOn(issue.getUpdateDate())
                .postedBy(issue.getPostedBy().getId())
                .trackingNumber(issue.getTrackingNumber())
                .description(issue.getDescription())
                .build();
    }

    public static void convertMultipartToAttachmentToUpdate(List<MultipartFile> multipartFiles, Issue issue){
        issue.getAttachments().clear();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            Set<Attachment> attachmentEntities = multipartFiles.stream().map(file -> {
                try {
                    return Attachment.builder()
                            .flag(true)
                            .fileName(file.getOriginalFilename())
                            .fileData(file.getBytes())
                            .issue(issue)
                            .build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());

            issue.getAttachments().addAll(attachmentEntities);
        }
    }

    public static void convertMultipartToAttachmentToSave(List<MultipartFile> multipartFiles, Issue issue){
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            Set<Attachment> attachmentEntities = multipartFiles.stream().map(file -> {
                try {
                    return Attachment.builder()
                            .flag(true)
                            .fileName(file.getOriginalFilename())
                            .fileData(file.getBytes())
                            .issue(issue)
                            .build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());

            issue.setAttachments(attachmentEntities);
        }
    }
}
