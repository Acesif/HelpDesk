package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.AttachmentDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.service.AttachmentService;
import com.grs.helpdeskmodule.utils.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/issue/{id}")
    public Response<?> getAttachmentsByIssue(@PathVariable("id") Long id){
        Set<Attachment> attachments = attachmentService.findAllByIssue(id);

        if (attachments.isEmpty()) {
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("No attachments of the issue "+id+" has been found")
                    .data(null)
                    .build();
        }

        Set<AttachmentDTO> attachmentDTOS = IssueMapper.convertToAttachmentDTOs(attachments,id);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All attachments of the issue "+id+" has been fetched")
                .data(attachmentDTOS)
                .build();
    }

    @GetMapping("/{id}")
    public Response<?> getAttachmentById(@PathVariable("id") Long id){
        Attachment attachment = attachmentService.findById(id);

        if (attachment == null){
            return Response.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Attachment not found")
                    .data(null)
                    .build();
        }

        AttachmentDTO attachmentDTO = AttachmentDTO.builder()
                .fileName(attachment.getFileName())
                .fileData(Base64.getEncoder().encodeToString(attachment.getFileData()))
                .issue(id)
                .build();

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Attachment found")
                .data(attachmentDTO)
                .build();
    }
}
