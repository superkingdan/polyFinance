package com.jnshu;

import com.jnshu.dao.ProductMapper;
import com.jnshu.entity.Product;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@MapperScan(basePackages = "com/jnshu/dao")
public class Entry {
    public static void main(String[] args) {
        SpringApplication.run(Entry.class,args);

//        Product product=new Product();
//        product.setCreateAt(System.currentTimeMillis());
//        product.setCreateBy(1L);
//        product.setProductCode("ZXC");
//        product.setProductName("万箭齐发");
//        product.setInterestRate("0.18");
//        product.setDeadline(180);
//        product.setInvestmentAmount("50000");
//        product.setRateOfInterest(2);
//        product.setRefundStyle(0);
//        product.setStatus(1);
//        product.setMark(1);
//        product.setRemark("这个是新产品，安排一下");
//        product.setIsRecommend(1);
//        product.setIsLimitePurchase(0);
//        product.setMoreMessage("https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/823.png");
//        productMapper.addProduct(product);
//        System.out.println(product.getId());
    }
}
