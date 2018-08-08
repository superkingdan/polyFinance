package com.jnshu.dao3;

import com.jnshu.entity.Claims;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Claims相关sql语句 债权
 */
@Mapper
@Repository(value ="claimsMapper3")
public interface ClaimsMapper3 {

    /**
     * 根据id查找债权
     */
    @Select("Select lend_end_at from claims where id =#{claimsId}")
    Claims findClaimsById(long claimsId);

    /**
     * 修改状态为已到期
     */
    @Update("update claims set status=#{status} where id=#{id}")
    boolean updateStatus(Claims claims);
}
