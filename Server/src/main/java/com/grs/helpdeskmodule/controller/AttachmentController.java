package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.AttachmentDTO;
import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.service.AttachmentService;
import com.grs.helpdeskmodule.utils.AttachmentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    /**
     * Retrieves all attachments associated with a specific issue by its ID.
     * Returns a message indicating no attachments are found if the issue has none.
     * Handles HTTP GET requests to the "/issue/{id}" endpoint.
     *
     * @param id The ID of the issue whose attachments are to be retrieved.
     * @return A response containing a set of attachment details or a message if no attachments are found.
     */
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

        Set<AttachmentDTO> attachmentDTOS = AttachmentUtils.convertToAttachmentDTOs(attachments,id);

        return Response.builder()
                .status(HttpStatus.OK)
                .message("All attachments of the issue "+id+" has been fetched")
                .data(attachmentDTOS)
                .build();
    }

    /**
     * Retrieves a specific attachment by its ID, returning the file data in Base64 format.
     * If the attachment does not exist, returns a message indicating it was not found.
     * Handles HTTP GET requests to the "/{id}" endpoint.
     *
     * @param id The ID of the attachment to retrieve.
     * @return A response containing the attachment details, including the file name and Base64-encoded data,
     *         or a message if the attachment is not found.
     */
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
                .filePath(attachment.getFilePath())
                .issue(id)
                .build();

        return Response.builder()
                .status(HttpStatus.OK)
                .message("Attachment found")
                .data(attachmentDTO)
                .build();
    }
}
