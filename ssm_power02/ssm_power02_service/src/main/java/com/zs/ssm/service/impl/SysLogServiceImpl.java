package com.zs.ssm.service.impl;

import com.zs.ssm.dao.SysLogDao;
import com.zs.ssm.pojo.SysLog;
import com.zs.ssm.service.SysLogService;
import org.apache.log4j.net.SyslogAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public void save(SysLog sysLog) throws Exception {
        sysLogDao.save(sysLog);
    }
}
