package com.jnshu.dao;

import com.jnshu.dto.ProductListRPO;
import com.jnshu.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.javassist.runtime.Desc;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * product相关sql语句，by王琦超
 */
@Mapper
@Component(value ="productMapper")
public interface ProductMapper {

//插入产品
    @Insert("insert into product (create_at,create_by,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,refund_style,remark,status,mark,is_recommend,is_limite_purchase,more_message) values (#{createAt},#{createBy},#{productCode},#{productName},#{interestRate},#{deadline},#{investmentAmount},#{rateOfInterest},#{refundStyle},#{remark},#{status},#{mark},#{isRecommend},#{isLimitePurchase},#{moreMessage})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addProduct(Product product);

    //产品上下架
    @Update("update product set status=#{status},update_at=#{updateAt},update_by=#{updateBy} where id=#{id}")
    int updateProductStatus(Product product);

    //修改产品信息
    @Update("update product set mark=#{mark},is_recommend=#{isRecommend},is_limite_purchase=#{isLimitePurchase},update_at=#{updateAt},update_by=#{updateBy} where id=#{id}")
    int updateProduct(Product product);

    //根据id查找产品详情
    @Select("select id,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,refund_style,mark,is_recommend,is_limite_purchase,more_message from product where id=#{id} ")
    Product getProductById(long id);

    //获得产品列表
    //前台获取需手动将status设置为0在售,并且设置是否推荐
    @SelectProvider(type = GetProductListByRpo.class,method = "getProductListByRpo")
    List<Product> getProductListByRpo(ProductListRPO productListRPO);
    class  GetProductListByRpo{
        public String getProductListByRpo(ProductListRPO rpo){
            return new SQL(){{
                SELECT("id,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,status,is_recommend,mark");
                FROM("product");
                if (rpo.getStatus()!=null)
                    WHERE("status=#{status}");
                if (rpo.getProductName()!=null)
                    WHERE("product_name=#{productName}");
                if (rpo.getProductCode()!=null)
                    WHERE("product_code=#{productCode}");
                if (rpo.getRateOfInterest()!=null)
                    WHERE("rate_of_interest=#{rateOfInterest}");
                if (rpo.getInterestRateMin()!=null)
                    WHERE("interest_rate>#{interestRateMin}");
                if(rpo.getInterestRateMax()!=null)
                    WHERE("interest_rate<#{interestRateMax}");
                if(rpo.getInvestmentAmount()!=null)
                    WHERE("investment_amount=#{investAmount}");
                if (rpo.getDeadlineMin()!=null)
                    WHERE("deadline>#{deadlineMin}");
                if(rpo.getDeadlineMax()!=null)
                    WHERE("deadline<#{deadlineMax}");
                if (rpo.getIsRecommend()!=null)
                    WHERE("is_recommend=#{isRecommend}");
                ORDER_BY("update_at desc");
            }}.toString();
        }
    }
}
