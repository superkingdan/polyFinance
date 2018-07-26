package com.jnshu.dao;

import com.jnshu.Entry;
import com.jnshu.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//classes后面是启动类.class
@SpringBootTest(classes = Entry.class)
public class ProductMapperTest {
   @Autowired
   ProductMapper productMapper;
    @Test
    public void addProduct() {
        Product product=new Product();
        product.setCreateAt(System.currentTimeMillis());
        product.setCreateBy(1L);
        product.setProductCode("SSS");
        product.setProductName("万箭齐发");
        product.setInterestRate("0.18");
        product.setDeadline(180);
        product.setInvestmentAmount("50000");
        product.setRateOfInterest(2);
        product.setRefundStyle(0);
        product.setStatus(1);
        product.setMark(1);
        product.setRemark("这个是新产品，安排一下");
        product.setIsRecommend(1);
        product.setIsLimitePurchase(0);
        product.setMoreMessage("https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/823.png");
        productMapper.addProduct(product);
        System.out.println(product.getId());
    }
}