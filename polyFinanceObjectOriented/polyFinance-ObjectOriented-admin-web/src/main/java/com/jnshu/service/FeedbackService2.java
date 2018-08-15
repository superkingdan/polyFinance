package com.jnshu.service;

import com.jnshu.Domain2.DomainFeedBackDetail;
import com.jnshu.dto2.FeedbackRPO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */
@Component(value = "feedbackService2")
public interface FeedbackService2 {

    List<DomainFeedBackDetail> getFeedbackList(Integer pageNum,Integer pageSize,FeedbackRPO rpo) throws Exception;
    List<DomainFeedBackDetail> getFeedbackList2(FeedbackRPO rpo) throws Exception;

    DomainFeedBackDetail getFeedbackDetail(Long id) throws Exception;

    Boolean deleteFeedback(Long id) throws Exception;
}
