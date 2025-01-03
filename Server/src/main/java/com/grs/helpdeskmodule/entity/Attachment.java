package com.grs.helpdeskmodule.entity;

import com.grs.helpdeskmodule.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Attachment extends BaseEntity {

    private String fileName;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;
}
