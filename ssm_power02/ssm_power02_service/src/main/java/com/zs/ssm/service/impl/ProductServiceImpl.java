package com.zs.ssm.service.impl;


import com.zs.ssm.dao.ProductDao;
import com.zs.ssm.pojo.Product;
import com.zs.ssm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;


    @Override
    public List<Product> findAll() throws Exception {

        return productDao.findAll();
    }

    @Override
    public void saveProduct(Product product) throws Exception {
        System.out.println(product.getDepartureTime());

        productDao.saveProduct(product);
    }
}
