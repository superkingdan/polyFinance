package com.jnshu.dao;

import com.jnshu.dto1.ProductListRPO;
import com.jnshu.dto1.StatisticsSalesListRO;
import com.jnshu.dto1.StatisticsSalesListRPO;
import com.jnshu.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * product相关sql语句
 * @author wangqichao
 */
@Mapper
@Component(value ="productMapper1")
public interface ProductMapper1 {

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
    @Select("select id,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,refund_style,mark,is_recommend,is_limite_purchase,more_message,remark from product where id=#{id} ")
    Product getProductById(long id);

    //根据相信产品名查找产品详情
    @Select("select id,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,refund_style,mark,is_recommend,is_limite_purchase,more_message,remark from product where product_name=#{productName} ")
    Product getProductByName(String productName);

    //获得指定id产品姓名
    @Select("select product_name from product where id=#{productId}")
    String getProductNameByProductId(long productId);

    //获得模糊产品名的产品id
    @Select("select id from product where product_name like \"%\"#{productName}\"%\"")
     Long[] getProductIdByProductName(String productName);

    //根据产品id查找产品还款类型
    @Select("select refund_style,deadline,product_name,interest_rate,rate_of_interest,product_code from product where id=#{id}")
    Product getRefundInfoById(long id);

    //根据产品id查找产品支付相关信息
    @Select("select product_name,deadline,interest_rate,is_limite_purchase,refund_style from product where id=#{productId}")
    Product getPayInfoById(long productId);

    //查找产品创建时间
    @Select("select create_at from product where id=#{id}")
    Long getProductCreateAtById(long id);

    //前台获得产品列表
//    @SelectProvider(type = ProductProvider.class,method = "getProductListByIsRecommend")
    @Select("select id,product_name,interest_rate,deadline,investment_amount,mark from product where status=0 and where is_recommend=1 order by update_at desc")
    List<Product> getProductListRecommend();

    @Select("select id,product_name,interest_rate,deadline,investment_amount,mark from product where status=0 order by update_at desc")
    List<Product> getProductList();

    //获得产品列表
    //前台获取需手动将status设置为0在售,并且设置是否推荐
    @SelectProvider(type = ProductProvider.class,method = "getProductListByRpo")
    List<Product> getProductListByRpo(ProductListRPO productListRPO);

    //根据产品name和code获得对应的信息
    @SelectProvider(type = ProductProvider.class,method = "getProductIdByProductNameAndCode")
    List<StatisticsSalesListRO> getProductIdByProductNameAndCode(StatisticsSalesListRPO rpo);

    class  ProductProvider{
//        public String getProductListByIsRecommend(Integer isRecommend){
//            return new SQL(){{
//                SELECT("id,product_name,interest_rate,deadline,investment_amount,mark");
//                FROM("product");
//                WHERE("status=0");
//                if(isRecommend!=null)
//                    WHERE("is_recommend=#{isRecommend}");
//                ORDER_BY("update_at desc");
//            }}.toString();
//        }

        public String getProductListByRpo(ProductListRPO rpo){
            return new SQL(){{
                SELECT("id,product_code,product_name,interest_rate,deadline,investment_amount,rate_of_interest,status,is_recommend,mark");
                FROM("product");
                if (rpo.getStatus()!=null)
                    WHERE("status=#{status}");
                if (rpo.getProductName()!=null)
                    WHERE("product_name like \"%\"#{productName}\"%\"");
                if (rpo.getProductCode()!=null)
                    WHERE("product_code like \"%\"#{productCode}\"%\"");
                if (rpo.getRateOfInterest()!=null)
                    WHERE("rate_of_interest=#{rateOfInterest}");
                if (rpo.getInterestRateMin()!=null)
                    WHERE("interest_rate>=#{interestRateMin}");
                if(rpo.getInterestRateMax()!=null)
                    WHERE("interest_rate<=#{interestRateMax}");
                if(rpo.getInvestmentAmount()!=null)
                    WHERE("investment_amount=#{investmentAmount}");
                if (rpo.getDeadlineMin()!=null)
                    WHERE("deadline>=#{deadlineMin}");
                if(rpo.getDeadlineMax()!=null)
                    WHERE("deadline<=#{deadlineMax}");
                if (rpo.getIsRecommend()!=null)
                    WHERE("is_recommend=#{isRecommend}");
                ORDER_BY("update_at desc");
            }}.toString();
        }
        public String getProductIdByProductNameAndCode(StatisticsSalesListRPO rpo){
            return new SQL(){{
                SELECT("id,product_code,product_name");
                FROM("product");
                if(rpo.getProductCode()!=null)
                    WHERE("product_code like \"%\"#{productCode}\"%\"");
                if(rpo.getProductName()!=null)
                    WHERE("product_name like \"%\"#{productName}\"%\"");
            }}.toString();
        }



    }
}
