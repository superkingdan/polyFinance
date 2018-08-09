package com.jnshu.dao3;

import com.jnshu.dto3.MessageListRPO;
import com.jnshu.entity.Message;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * message相关sql语句
 */
@Mapper
@Repository(value ="messageMapper3")
public interface MessageMapper3 {


    /**
     * 根据消息id查找
     */
     @Select("select * from message where id=#{id}")
     Message findById(long id);
    /**
     * 查找消息所有人
     */
     @Select("select * from message where sent_person_type=#{type} and is_sent=0 order by create_at")
     List<Message> findByTpye(@Param("type") int type);

    /**
     * 查找消息
     */
    @Select("select * from message where user_id=#{id}  and is_sent=0 order by create_at")
    List<Message> findAllByUser(@Param("id") long id);

    /**
     * 多条件查询
     */
    @SelectProvider(type = FindMessageListRPO.class,method = "findMessageListRPO")
    List<MessageListRPO> findMessageListRPO(MessageListRPO messageListRPO);
    class  FindMessageListRPO{
        public String findMessageListRPO(MessageListRPO rpo){
            if (rpo.getPageNum()==0){rpo.setPageNum(1);}
            if (rpo.getPageSize()==0){rpo.setPageSize(10);}
            return new SQL(){{
                SELECT("id,create_at,create_by,update_at,update_by,title,content,sent_person_type,message_type,is_push,is_sent,transaction_id,user_id");
                FROM("message");
                if (rpo.getUserId()==0)
                    WHERE("user_id=0");
                if (rpo.getUserId()!=0)
                    WHERE("user_id=#{userId}");
                if (rpo.getCreateMin()!=0)
                    WHERE("create_at>=#{createMin}");
                if (rpo.getCreateMax()!=0)
                    WHERE("create_at<=#{createMax}");
                if (rpo.getCreateBy()!=0)
                    WHERE("create_by=#{createBy}");
                if(rpo.getIsSent()!=3)
                    WHERE("is_sent=#{isSent}");
                if(rpo.getSentPersonType()!=2)
                    WHERE("sent_person_type=#{sentPersonType}");
                if (rpo.getTitle()!=null)
                    WHERE("title like \"%\"#{title}\"%\"");
                ORDER_BY("create_at desc");
            }}.toString();
        }
    }




     /**
      * 删除消息
      */
     @Delete("delete from message where id=#{id}")
     boolean deleteMessage(long id);
     /**
      * 添加消息
      */
     @Insert("insert into message (create_at,create_by,title,content,sent_person_type,message_type,is_push,is_sent,transaction_id,user_id,introduce) " +
             "values (#{createAt},#{createBy},#{title},#{content},#{sentPersonType},#{messageType},#{isPush},#{isSent},#{transactionId},#{userId},#{introduce})")
     @Options(useGeneratedKeys=true,keyProperty="id")
     int addMessage(Message message);

    /**
     * 修改资料(all)
     */
    @UpdateProvider(type = UpdateMessage.class,method = "updateMessage")
    boolean updateMessage(Message message);
    class  UpdateMessage{
        public String updateMessage(Message message){
            return new SQL(){{
                UPDATE("message");
                if (message.getUpdateAt()!=0){
                    SET("update_at=#{updateAt}");}
                if (message.getUpdateBy()!=0){
                    SET("update_by=#{updateBy}");}
                if (message.getTitle()!=null){
                    SET("title=#{title}");}
                if (message.getContent()!=null){
                    SET("content=#{content}");}
                if (message.getSentPersonType()!=0){
                    SET("sent_person_type=#{sentPersonType}");}
                if (message.getMessageType()!=0){
                    SET("message_type=#{messageType}");}
                if (message.getIsPush()!=0){
                    SET("is_push=#{isPush}");}
                if (message.getIsSent()!=0){
                    SET("is_sent=#{isSent}");}
                if (message.getTransactionId()!=0){
                    SET("transaction_id=#{transactionId}");}
                if (message.getUserId()!=0){
                    SET("user_id=#{userId}");}
                WHERE("id=#{id}");
            }}.toString();
        }
    }


}
