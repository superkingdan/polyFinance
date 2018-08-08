package com.jnshu.dao3;

import com.jnshu.entity.Feedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * feedback相关sql语句
 */
@Mapper
@Repository(value ="feedbackMapper3")
public interface FeedbackMapper3 {

//   意见反馈： insert into feedback(user.id,user.phone_name,user.email,creat_at) values()

    @Insert("insert into feedback (create_at,create_by,user_id,content) " +
            "values (#{createAt},#{createBy},#{userId},#{content})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addFeedback(Feedback feedback);
}
