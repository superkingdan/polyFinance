package com.jnshu.dao3;

import com.jnshu.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Product相关sql语句
 */
@Mapper
@Repository(value ="productMapper3")
public interface ProductMapper3 {

    /**
     * 根据产品id获取产品名 定时任务需要
     */
    @Select("Select product_name from product where id=#{id}")
    Product findProductName(long id);
    /**
     * 根据交易id获取产品名 定时任务需要
     */
    @Select("Select product.product_name from product inner join transaction on " +
            "product.id=transaction.product_id where transaction.id=#{id}")
    Product findByTransactionId(long id);
}
