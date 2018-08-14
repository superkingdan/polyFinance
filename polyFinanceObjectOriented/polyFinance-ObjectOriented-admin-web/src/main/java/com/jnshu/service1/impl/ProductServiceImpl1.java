package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.ProductMapper1;
import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ProductService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * product相关service
 * @author wangqichao
 */
@Service
//开启事务
@Transactional
public class ProductServiceImpl1 implements ProductService1 {
    @Autowired
    ProductMapper1 productMapper1;

    private static final Logger log= LoggerFactory.getLogger(ProductService1.class);
    /**
     * 获得产品分页列表
     * @param rpo 产品分页条件
     * @return 产品列表
     */
    @Override
    public Page<Product> getProductList(ProductListRPO rpo) throws Exception{
        log.info("获得产品列表，查询条件为"+rpo);
        Page<Product> products= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        try{
            productMapper1.getProductListByRpo(rpo);
        }catch (Exception e){
            throw new MyException(-1,"获得产品列表失败");
        }
        return products;
    }

    /**
     * 获得指定产品详情
     * @param id 产品id
     * @return 产品详情
     */
    @Override
    public Product getProductById(long id) throws Exception{
        log.info("获得指定产品"+id+"详情");
        Product product;
        try{
            product=productMapper1.getProductById(id);
        }catch (Exception e){
            throw new MyException(-1,"获得产品"+id+"详情失败");
        }
        return product;
    }

    /**
     * 新增产品
     * @param product 新增产品内容
     * @return 操作结果
     */
    @Override
    public int addProduct(Product product) throws Exception{
        log.info("新增产品，信息为"+product);
        product.setCreateAt(System.currentTimeMillis());
        product.setStatus(Product.STATUS_STOP_SALE);
        try{
            productMapper1.addProduct(product);
        }catch (Exception e){
            throw new MyException(-1,"新增产品失败");
        }
        long id=product.getId();
        log.info("新增产品id为"+id);
        return 1;
    }

    /**
     * 修改产品信息
     * @param product 需变为的产品信息
     * @return 修改结果
     */
    @Override
    public int updateProduct(Product product) throws Exception{
        log.info("修改产品信息"+product);
        product.setUpdateAt(System.currentTimeMillis());
        try{
            productMapper1.updateProduct(product);
        }catch (Exception e){
            throw new MyException(-1,"更新产品"+product.getId()+"失败");
        }
        return 1;
    }

    /**
     * 修改产品在售状态
     * @param product 需变为的产品信息
     * @return 修改结果
     */
    @Override
    public int updateProductStatus(Product product) throws Exception{
        log.info("修改产品"+product.getId()+"状态为"+product.getStatus());
        product.setUpdateAt(System.currentTimeMillis());
        try{
            productMapper1.updateProductStatus(product);
        }catch (Exception e){
            throw new MyException(-1,"修改产品"+product.getId()+"状态失败");
        }
        return 1;
    }
}
