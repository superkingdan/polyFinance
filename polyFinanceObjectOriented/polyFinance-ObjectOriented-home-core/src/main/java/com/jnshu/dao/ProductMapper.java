package com.jnshu.dao;

import com.jnshu.entity.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * product相关sql语句
 */
@Mapper
public interface ProductMapper {

//插入产品
    @Insert("insert into product (create_at,create_by,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,refund_style,remark,status,mark,is_recommend,is_limite_purchase,more_message) values (#{createAt},#{createBy},#{productCode},#{productName},#{interestRate},#{deadline},#{investmentAmount},#{rateOfInterest},#{refundStyle},#{remark},#{status},#{mark},#{isRecommend},#{isLimitePurchase},#{moreMessage})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addProduct(Product product);
}
