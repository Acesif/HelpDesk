package com.grs.helpdeskmodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "issue_replies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueReplies implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    protected Long id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    protected Date createDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Dhaka")
    @UpdateTimestamp
    @Column(name = "updated_at")
    protected Date updateDate;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    protected Boolean flag = true;

    public Long repliantId;
    public Long parentIssueId;
    public String comment;
    @Enumerated(EnumType.STRING)
    public IssueStatus updatedStatus;
}