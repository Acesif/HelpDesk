package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.entity.Attachment;
import com.grs.helpdeskmodule.entity.Issue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    public static void parseAttachment(Issue issue, List<Map<String, Object>> transformedAttachments) {
        if (issue.getAttachments() != null && !issue.getAttachments().isEmpty()) {
            for (Attachment a : issue.getAttachments()) {
                Map<String, Object> attachmentData = new HashMap<>();
                attachmentData.put("fileName", a.getFileName());
                attachmentData.put("id", a.getId());
                transformedAttachments.add(attachmentData);
            }
        }
    }
}
