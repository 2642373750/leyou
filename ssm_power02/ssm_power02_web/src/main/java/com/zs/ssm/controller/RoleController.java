package com.zs.ssm.controller;

import com.zs.ssm.pojo.Role;
import com.zs.ssm.service.RoleService;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.rmi.MarshalledObject;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception {

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("roleList",roleService.findAll());
        modelAndView.setViewName("role-list");
        return modelAndView;
    }

    @RequestMapping("/save.do")
    public String save(Role role) throws Exception {

        roleService.save(role);
        return "redirect:findAll.do";
    }

}
