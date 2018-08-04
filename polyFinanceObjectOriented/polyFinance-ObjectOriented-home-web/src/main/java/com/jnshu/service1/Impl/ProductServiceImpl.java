package com.jnshu.service1.Impl;

import com.jnshu.dao.ProductMapper;
import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;
import com.jnshu.service1.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品相关service
 * @author wangqichao
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService{
    private static final Logger log= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductMapper productMapper;

    /**
     * 获得产品列表
     * @param rpo 查询条件，主要是区分是否需要推荐
     * @return 查询结果
     */
    @Override
    public List<Product> getProductList(ProductListRPO rpo) {
        //设置状态为在售
        rpo.setStatus(0);
        return productMapper.getProductListByRpo(rpo);
    }

    /**
     * 获得指定产品详情
     * @param id 产品id
     * @return 产品详情
     */
    @Override
    public Product getProductById(long id) {
        return productMapper.getProductById(id);
    }
}
