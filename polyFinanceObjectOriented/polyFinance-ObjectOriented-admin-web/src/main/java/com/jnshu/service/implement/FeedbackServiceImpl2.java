package com.jnshu.service.implement;

import com.jnshu.Domain2.DomainFeedBackDetail;
import com.jnshu.dao2.FeedbackMapper2;
import com.jnshu.dto2.FeedbackRPO;
import com.jnshu.service.FeedbackService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */

@Service
@Component
public class FeedbackServiceImpl2 implements FeedbackService2 {

    @Autowired
    FeedbackMapper2 feedbackMapper2;
    @Override
    public List<DomainFeedBackDetail> getFeedbackList(FeedbackRPO rpo) throws Exception {
        return feedbackMapper2.getFeedbackList(rpo);
    }

    @Override
    public DomainFeedBackDetail getFeedbackDetail(Long id) throws Exception {
        return feedbackMapper2.getFeedbackDetail(id);
    }

    @Override
    public Boolean deleteFeedback(Long id) throws Exception {
        return feedbackMapper2.deleteFeedback(id);
    }
}
