package com.zs.ssm.dao;

import com.zs.ssm.pojo.Member;
import com.zs.ssm.pojo.Orders;
import com.zs.ssm.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrdersDao {

    @Select("select * from orders")
    @Results({
            @Result(id=true,property = "id",column="id"),
            @Result(property = "orderNum",column="orderNum"),
            @Result(property = "orderTime",column="orderTime"),
            @Result(property = "orderStatus",column="orderStatus"),
            @Result(property = "peopleCount",column="peopleCount"),
            @Result(property = "payType",column="payType"),
            @Result(property = "orderDesc",column="orderDesc"),
            @Result(property = "product",column="productId",javaType= Product.class,one = @One(select ="com.zs.ssm.dao.ProductDao.findById" ))
    })
    public List<Orders> findAll() throws Exception;

    @Select("select * from orders where id=#{id}")
    @Results({
            @Result(id=true,property = "id",column="id"),
            @Result(property = "orderNum",column="orderNum"),
            @Result(property = "orderTime",column="orderTime"),
            @Result(property = "orderStatus",column="orderStatus"),
            @Result(property = "peopleCount",column="peopleCount"),
            @Result(property = "payType",column="payType"),
            @Result(property = "orderDesc",column="orderDesc"),
            @Result(property = "product",column="productId",javaType= Product.class,one = @One(select ="com.zs.ssm.dao.ProductDao.findById" )),
            @Result(property = "member",column = "memberId",javaType = Member.class,one = @One(select = "com.zs.ssm.dao.MemberDao.findById")),
            @Result(property = "travellers",column = "id",javaType = java.util.List.class,many =@Many(select = "com.zs.ssm.dao.TravellerDao.findAll"))
    })
    public Orders findById(String id) throws Exception;
}
