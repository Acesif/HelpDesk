package com.grs.helpdeskmodule.dto;

import com.grs.helpdeskmodule.entity.Issue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDTO {
    private String fileName;
    private String fileData;
    private Long issue;
}
