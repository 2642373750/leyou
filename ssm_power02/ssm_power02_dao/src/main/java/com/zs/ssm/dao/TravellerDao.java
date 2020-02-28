package com.zs.ssm.dao;

import com.zs.ssm.pojo.Traveller;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TravellerDao {

    @Select("select * from traveller t where t.id in (select travellerId from order_traveller ot where ot.orderId=#{id})")
    public List<Traveller> findAll(String id) throws Exception;
}
