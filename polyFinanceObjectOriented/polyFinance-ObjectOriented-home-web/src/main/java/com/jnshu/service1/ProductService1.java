package com.jnshu.service1;

import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;

import java.util.List;

public interface ProductService1 {

    List<Product> getProductList(Integer isRecommend)throws Exception;

    Product getProductById(long id)throws Exception;
}
