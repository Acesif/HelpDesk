package com.grs.helpdeskmodule.dto;

import com.grs.helpdeskmodule.entity.Issue;
import lombok.Data;

@Data
public class AttachmentDTO {
    private String fileName;
    private String fileData;
    private Issue issue;
}
