package com.zs.ssm.dao;

import com.zs.ssm.pojo.SysLog;
import org.apache.ibatis.annotations.Insert;

public interface SysLogDao {

    @Insert("insert into syslog(visitTime,username,ip,url,executionTime,method) values(#{visitTime},#{username},#{ip},#{url},#{executionTime},#{method})")
    public void save(SysLog sysLog) throws Exception;
}
