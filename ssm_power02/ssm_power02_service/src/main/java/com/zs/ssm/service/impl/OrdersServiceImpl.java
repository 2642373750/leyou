package com.zs.ssm.service.impl;


import com.github.pagehelper.PageHelper;
import com.zs.ssm.dao.OrdersDao;
import com.zs.ssm.pojo.Orders;
import com.zs.ssm.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersDao ordersDao;


    @Override
    public List<Orders> findAll(int page, int pageSize) throws Exception {
        //分页
        PageHelper.startPage(page,pageSize);
        return ordersDao.findAll();
    }

    @Override
    public Orders findById(String id) throws Exception {
        return ordersDao.findById(id);
    }



}
