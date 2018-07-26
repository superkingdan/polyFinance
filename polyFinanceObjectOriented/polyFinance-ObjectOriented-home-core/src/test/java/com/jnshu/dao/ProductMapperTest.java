package com.jnshu.dao;

import com.jnshu.Entry;
import com.jnshu.dto.ProductListRPO;
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
        product.setProductCode("ABC");
        product.setProductName("万箭齐发");
        product.setInterestRate("0.05");
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

    @Test
    public void updateProductStatus(){
        Product product=new Product();
        product.setId(1);
        product.setUpdateAt(System.currentTimeMillis());
        product.setUpdateBy(1L);
        product.setStatus(0);
        productMapper.updateProductStatus(product);
    }

    @Test
    public void updateProduct(){
        Product product=new Product();
        product.setId(3);
        product.setUpdateAt(System.currentTimeMillis());
        product.setUpdateBy(1L);
        product.setMark(2);
        product.setIsLimitePurchase(1);
        product.setIsRecommend(0);
        productMapper.updateProduct(product);
    }

    @Test
    public void getProductById(){
        System.out.println(productMapper.getProductById(1));
    }

    @Test
    public void getProductListByRpo(){
        ProductListRPO rpo=new ProductListRPO();
        rpo.setInterestRateMax("0.20");
        rpo.setInterestRateMin("0.06");
        System.out.println(productMapper.getProductListByRpo(rpo));
    }
}