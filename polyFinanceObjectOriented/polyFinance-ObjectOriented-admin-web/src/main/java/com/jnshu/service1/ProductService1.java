package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "productService1")
public interface ProductService1 {
    Page<Product> getProductList(ProductListRPO rpo)throws Exception;

    Product getProductById(long id)throws Exception;

    int addProduct(Product product)throws Exception;

    int updateProduct(Product product)throws Exception;

    int updateProductStatus(Product product)throws Exception;
}
