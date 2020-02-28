package com.zs.ssm.dao;

import com.zs.ssm.pojo.Member;
import org.apache.ibatis.annotations.Select;

public interface MemberDao {

    @Select("select * from member where id=#{id}")
    public Member findById(String id) throws Exception;
}
