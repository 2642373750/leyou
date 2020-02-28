package com.zs.ssm.controller;

import com.zs.ssm.pojo.Permission;
import com.zs.ssm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping(value="findAll.do")
    public ModelAndView findAll() throws Exception {

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("permission-list");
        modelAndView.addObject("permissionList",permissionService.findAll());
        return modelAndView;

    }

}
