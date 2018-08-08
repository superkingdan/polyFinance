package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "productService1")
public interface ProductService1 {
    Page<Product> getProductList(ProductListRPO rpo);

    Product getProductById(long id);

    int addProduct(Product product);

    int updateProduct(Product product);

    int updateProductStatus(Product product);
}
