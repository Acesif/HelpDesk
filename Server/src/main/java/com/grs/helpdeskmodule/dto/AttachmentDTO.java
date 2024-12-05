package com.grs.helpdeskmodule.dto;

import com.grs.helpdeskmodule.entity.Issue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private Long issue;
}
