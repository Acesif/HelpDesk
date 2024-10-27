package com.grs.helpdeskmodule.dto;

import com.grs.helpdeskmodule.entity.IssueStatus;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {

    private String trackingNumber;
    private String title;
    private String description;
    private IssueStatus status;
    private Long postedBy;
    private Date postedOn;
    private Set<AttachmentDTO> attachments;
}
