package com.grs.helpdeskmodule.dto;

import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.IssueStatus;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDto {

    private String trackingNumber;

    private String title;

    private String description;

    private IssueStatus status;

    private String postedBy;

    private Date postedOn;

    private Set<Attachment> attachments;
}
