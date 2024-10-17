package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.entity.IssueReplies;
import com.grs.helpdeskmodule.repository.IssueReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueReplyService {

    private final IssueReplyRepository issueReplyRepository;

    public IssueReplies saveReply(IssueReplies issueReplies){
        return issueReplyRepository.save(issueReplies);
    }
}
