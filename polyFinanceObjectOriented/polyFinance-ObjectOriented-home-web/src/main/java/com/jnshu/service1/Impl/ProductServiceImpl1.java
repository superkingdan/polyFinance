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
     * 获得所有推荐产品列表
     * @return 产品列表
     * @throws Exception
     */
    @Override
//    @Cacheable(value = "object-productList-recommend")
    public List<Product> getProductListRecommend() throws Exception {
        long st=System.currentTimeMillis();
        log.info("获得推荐产品列表");
        //设置状态为在售
        List<Product> list;
        try {
            list = productMapper1.getProductListRecommend();
        }catch (Exception e){
            throw new MyException(-1,"获得产品列表失败");
        }
        long et=System.currentTimeMillis();
        log.info("本次查询时间为{}",(et-st));
        return list;
    }

    /**
     * 获得所有在售产品列表
     * @return 产品列表
     * @throws Exception
     */
    @Override
//    @Cacheable(value = "object-productList")
    public List<Product> getProductList() throws Exception {
        long st=System.currentTimeMillis();
        log.info("获得所有在售产品列表");
        //设置状态为在售
        List<Product> list;
        try {
            list = productMapper1.getProductList();
        }catch (Exception e){
            throw new MyException(-1,"获得产品列表失败");
        }
        long et=System.currentTimeMillis();
        log.info("本次查询时间为{}",(et-st));
        return list;
    }

    /**
     * 获得指定产品详情
     * @param id 产品id
     * @return 产品详情
     */
    @Override
//    @Cacheable(value = "object-product",key ="#id")
    public Product getProductById(long id) throws Exception{
        long st=System.currentTimeMillis();
        log.info("获得指定id为"+id+"的产品");
        Product product;
        try{
            product=productMapper1.getProductById(id);
        }catch (Exception e){
            throw new MyException(-1,"获取指定产品信息失败");
        }
        long et=System.currentTimeMillis();
        log.info("本次查询时间为{}",(et-st));
        return product;
    }
}
