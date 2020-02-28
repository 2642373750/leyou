package com.zs.ssm.service;

import com.zs.ssm.pojo.Product;

import java.util.List;

public interface ProductService {

    public List<Product> findAll() throws Exception;

    public void saveProduct(Product product) throws Exception;
}
