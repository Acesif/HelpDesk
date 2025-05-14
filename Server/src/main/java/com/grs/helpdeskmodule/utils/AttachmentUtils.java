package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.dto.AttachmentDTO;
import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AttachmentUtils extends CommonUtils{

    public static Set<AttachmentDTO> convertToAttachmentDTOs(Set<Attachment> attachments,Long id) {
        return attachments.stream().map(attachment ->
            AttachmentDTO.builder()
                    .id(attachment.getId())
                    .fileName(attachment.getFileName())
                    .filePath(attachment.getFilePath())
                    .issue(id)
                    .build()
        ).collect(Collectors.toSet());
    }

    public static Set<Attachment> convertToAttachments(Set<AttachmentDTO> attachmentDTOs) {
        return attachmentDTOs.stream().map(dto -> {
            Attachment attachment = new Attachment();
            attachment.setFileName(dto.getFileName());
            attachment.setFilePath(dto.getFilePath());
            return attachment;
        }).collect(Collectors.toSet());
    }

    public static void convertMultipartToAttachmentToUpdate(List<MultipartFile> multipartFiles, Issue issue) {
        try {
            if (issue.getAttachments() != null && !issue.getAttachments().isEmpty()) {
                for (Attachment oldAttachment : issue.getAttachments()) {
                    String oldFilePath = oldAttachment.getFilePath();
                    String deletedFilePath = getAttachmentFilePath(oldAttachment.getFileName(), issue.getTrackingNumber(), true);

                    relocateFiles(oldFilePath, deletedFilePath);
                }
            }

            if (issue.getAttachments() != null && !issue.getAttachments().isEmpty()) {
                issue.getAttachments().clear();
            }

            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                Set<Attachment> attachmentEntities = multipartFiles.stream().map(file -> {
                    try {
                        String newFilePath = getAttachmentFilePath(file.getOriginalFilename(), issue.getTrackingNumber(), false);

                        saveFileToPath(file, newFilePath);

                        return Attachment.builder()
                                .flag(true)
                                .fileName(file.getOriginalFilename())
                                .filePath(newFilePath)
                                .issue(issue)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process file: " + file.getOriginalFilename(), e);
                    }
                }).collect(Collectors.toSet());

                issue.getAttachments().addAll(attachmentEntities);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating attachments for issue: " + issue.getTrackingNumber(), e);
        }
    }

    public static void convertMultipartToAttachmentToSave(List<MultipartFile> multipartFiles, Issue issue){
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            Set<Attachment> attachmentEntities = multipartFiles.stream().map(file -> {

                try{
                    String filePath = getAttachmentFilePath(file.getOriginalFilename(),issue.getTrackingNumber(), false);
                    saveFileToPath(file,filePath);

                    return Attachment.builder()
                            .flag(true)
                            .fileName(file.getOriginalFilename())
                            .filePath(filePath)
                            .issue(issue)
                            .build();
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());

            issue.setAttachments(attachmentEntities);
        }
    }

    public static String getAttachmentFilePath(String fileName, String trackingNumber, boolean delete) {

        try{
            Path issueAttachmentPath = Paths.get("uploads", "issue-attachments", trackingNumber);
            if (delete){
                issueAttachmentPath = Paths.get("uploads", "issue-attachments", "deletedFiles", trackingNumber);
            }
            if (!Files.exists(issueAttachmentPath)) {
                Files.createDirectories(issueAttachmentPath);
            }
            Path absoluteAttachmentPath = issueAttachmentPath.toAbsolutePath();

            return absoluteAttachmentPath.resolve(fileName).toString();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String generateTrackingNumber() {
        SecureRandom random = new SecureRandom();
        long number = (long) (random.nextDouble() * 1_000_000_00000L);
        return String.format("%011d", number);
    }

    private static void saveFileToPath(
            MultipartFile file,
            String filePath
    ) throws IOException {
        Path path = Paths.get(filePath);
        file.transferTo(path.toFile());
    }

    private static void relocateFiles(
            String oldPath,
            String newPath
    ) throws IOException {
        Path sourcePath = Paths.get(oldPath);
        Path targetPath = Paths.get(newPath);

        if (!Files.exists(targetPath.getParent())) {
            Files.createDirectories(targetPath.getParent());
        }
        Files.move(sourcePath, targetPath);
    }

    public static List<Map<String, Object>> getAttachmentFilenames(Issue issue){
        List<Map<String, Object>> transformedAttachments = new ArrayList<>();

        parseAttachment(issue, transformedAttachments);
        return transformedAttachments.stream().toList();
    }
}
