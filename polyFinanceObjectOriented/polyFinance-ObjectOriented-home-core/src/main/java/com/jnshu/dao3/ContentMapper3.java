package com.jnshu.dao3;


import com.jnshu.entity.Content;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Content相关sql语句 内容-推荐banner
 */
@Mapper
@Repository(value ="contentMapper3")
public interface ContentMapper3 {
    /**
     * banner 集合
     */
    @Select(" Select * from content where status=1 and type=#{type} order by create_at desc")
    List<Content> findToList(int type);

    /**
     *  关于我们 查看帮助
     */
    @Select(" Select * from content where status=1 and type=#{type} order by create_at desc")
    Content findContent(int type);


}
