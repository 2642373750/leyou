package com.zs.ssm.service;

import com.zs.ssm.pojo.Orders;

import java.util.List;

public interface OrdersService {

    public List<Orders> findAll(int page,int pageSize) throws Exception;

    public Orders findById(String id) throws Exception;
}
