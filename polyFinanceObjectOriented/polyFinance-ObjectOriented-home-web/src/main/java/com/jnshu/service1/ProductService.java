package com.jnshu.service1;

import com.jnshu.dto1.ProductListRPO;
import com.jnshu.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProductList(ProductListRPO rpo);

    Product getProductById(long id);
}
