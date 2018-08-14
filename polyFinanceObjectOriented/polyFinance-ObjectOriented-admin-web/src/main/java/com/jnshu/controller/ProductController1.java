package com.jnshu.controller;

import com.github.pagehelper.Page;
import com.jnshu.entity.Product;
import com.jnshu.dto1.ProductListRPO;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ProductService1;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 产品模块相关接口，2E
 * @author wangqichao
 */
@RestController
public class ProductController1 {
    private static final Logger log= LoggerFactory.getLogger(ProductController1.class);
@Autowired
ProductService1 productService1;
    /**
     * 获得产品列表
     * @param rpo 查询条件
     * @return 返回参数，包括code,message,total,size,产品列表
     */
    @GetMapping(value = "/a/u/product/list")
    public Map getProductList(@ModelAttribute ProductListRPO rpo)throws Exception{
        log.info("获得产品列表，查询条件为"+rpo);
        Page<Product> products= productService1.getProductList(rpo);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        map.put("total",products.getTotal());
        map.put("size",rpo.getSize());
        map.put("data",products);
        return map;
    }

    /**
     * 按照产品id获得指定产品详情
     * @param id 产品id
     * @return 返回参数，code,message,产品详情
     */
    @GetMapping(value = "/a/u/product/{id}")
    public Map getProduct(@PathVariable(value = "id")long id)throws Exception{
        log.info("获得指定产品详情，产品id为"+id);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        Product product= productService1.getProductById(id);
        map.put("data",product);
        return map;
    }

    /**
     * 修改指定产品
     * @param id 产品id
     * @param product 修改产品的内容
     * @return 修改结果，code,message
     */
    @PutMapping(value = "/a/u/product/{id}")
    public Map updateProduct(@PathVariable(value = "id")long id,@ModelAttribute Product product,HttpServletRequest request)throws Exception{
        if(product.getMark()==null||product.getIsLimitePurchase()==null||product.getIsRecommend()==null){
            throw new MyException(10002,"参数不能为空");
        }
        log.info("修改，产品id为"+id+"修改为"+product);
        product.setId(id);
        String updateByS=CookieUtil.getCookieValue(request,"uid");
        long updateBy = Long.parseLong(updateByS);
        product.setUpdateBy(updateBy);
        productService1.updateProduct(product);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 新增产品信息
     * @param product 新增产品的详情
     * @return 返回参数，code,message
     * 需要注意名称代号不能重复，需抓异常
     */
    @PostMapping(value = "/a/u/product")
    public Map addProduct(@ModelAttribute Product product, HttpServletRequest request)throws Exception{
        if(product.getProductCode()==null||product.getProductName()==null||product.getInterestRate()==null||product.getDeadline()==null||product.getInvestmentAmount()==null|| product.getRateOfInterest()==null||product.getRefundStyle()==null||product.getMark()==null||product.getIsRecommend()==null||product.getIsLimitePurchase()==null|| product.getMoreMessage()==null){
            throw new MyException(10002,"参数不能为空");
        }
        log.info("新增产品，产品详情为"+product);
        Map<String,Object> map=new HashMap<>();
        String createByS= CookieUtil.getCookieValue(request,"uid");
        long createBy = Long.parseLong(createByS);
        product.setCreateBy(createBy);
        productService1.addProduct(product);
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 修改产品状态
     * @param id 产品id
     * @param status 希望成为的状态
     * @return 修改结果，code,message
     */
    @PutMapping(value = "/a/u/product/{id}/status")
    public Map updateProductStatus(@PathVariable (value = "id")long id,@RequestParam(value = "status")Integer status,HttpServletRequest request)throws Exception{
        if(status==null){
            throw new MyException(10002,"参数不能为空");
        }
        log.info("修改产品"+id+"状态为"+status);
        Product product=new Product();
        product.setId(id);
        product.setStatus(status);
        String updateByS=CookieUtil.getCookieValue(request,"uid");
        long updateBy = Long.parseLong(updateByS);
        product.setUpdateBy(updateBy);
        productService1.updateProductStatus(product);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }



}
