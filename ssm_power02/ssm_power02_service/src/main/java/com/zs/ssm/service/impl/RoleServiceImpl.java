package com.zs.ssm.service.impl;

import com.zs.ssm.dao.RoleDao;
import com.zs.ssm.pojo.Role;
import com.zs.ssm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl  implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAll() throws Exception {

        return roleDao.findAll();
    }

    @Override
    public void save(Role role) throws Exception {
        roleDao.save(role);
    }
}
