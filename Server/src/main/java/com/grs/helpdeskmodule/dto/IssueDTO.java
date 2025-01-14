package com.grs.helpdeskmodule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grs.helpdeskmodule.entity.Issue;
import com.grs.helpdeskmodule.entity.IssueCategory;
import com.grs.helpdeskmodule.entity.IssueStatus;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {

    private Long id;
    private String trackingNumber;
    private String title;
    private String description;
    private IssueStatus status;
    private IssueCategory category;
    private Long postedBy;
    private Long officeId;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    private Date postedOn;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    private Date updatedOn;
    private List<Map<String, Object>> attachments;
}
