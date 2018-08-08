package com.jnshu.dao3;

import com.jnshu.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Contract相关sql语句 合同
 */
@Mapper
@Repository(value ="contractMapper3")
public interface ContractMapper3 {

    /**
     * 修改合同匹配状态
     */
    @Update("update contract set is_matching_claims=#{isMatchingClaims} where id=#{id}")
    boolean updateOder(Contract contract);

    /**
     * 查找合同
     */
    @Select("select * from contract where id=#{id}")
    Contract findById(long id);
    /**
     * 查找合同
     */
    @Select("select * from contract where contract_code=#{contractCode}")
    Contract findByCode(String contractCode);



}
