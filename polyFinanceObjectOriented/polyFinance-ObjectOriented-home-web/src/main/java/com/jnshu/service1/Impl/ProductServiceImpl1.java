package com.jnshu.service1.Impl;

import com.jnshu.dao.ProductMapper1;
import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ProductService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品相关service
 * @author wangqichao
 */
@Service
@Transactional
public class ProductServiceImpl1 implements ProductService1 {
    private static final Logger log= LoggerFactory.getLogger(ProductServiceImpl1.class);
    @Autowired
    ProductMapper1 productMapper1;

    /**
     * 获得产品列表
     * @param isRecommend 查询条件，主要是区分是否需要推荐
     * @return 查询结果
     */
    @Override
    public List<Product> getProductList(Integer isRecommend) throws Exception{
        log.info("获得产品列表");
        //设置状态为在售
        List<Product> list;
       try {
           list = productMapper1.getProductListByIsRecommend(isRecommend);
       }catch (Exception e){
           throw new MyException(-1,"获得产品列表失败");
       }
        return list;
    }

    /**
     * 获得指定产品详情
     * @param id 产品id
     * @return 产品详情
     */
    @Override
    @Cacheable(value = "object-product",key ="#id")
    public Product getProductById(long id) throws Exception{
        log.info("获得指定id为"+id+"的产品");
        Product product;
        try{
            product=productMapper1.getProductById(id);
        }catch (Exception e){
            throw new MyException(-1,"获取指定产品信息失败");
        }
        return product;
    }
}
