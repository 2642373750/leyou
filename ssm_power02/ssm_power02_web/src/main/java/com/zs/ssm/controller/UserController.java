package com.zs.ssm.controller;

import com.zs.ssm.pojo.Role;
import com.zs.ssm.pojo.UserInfo;
import com.zs.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/findAll.do")
    public ModelAndView findAll() throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        List<UserInfo> list=userService.findAll();
        modelAndView.setViewName("user-list");
        modelAndView.addObject("userList",list);
        return modelAndView;

    }

    @RequestMapping(value="/save.do")
    public String save(UserInfo userInfo) throws Exception {
        userService.save(userInfo);
        return "redirect:findAll.do";

    }

    @RequestMapping(value="/findById.do")
    public ModelAndView findById(String id) throws Exception {

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("user-show");
        UserInfo userInfo=userService.findById(id);
        modelAndView.addObject("user",userInfo);
        return modelAndView;

    }

    @RequestMapping(value="/findUserByIdAndAllRole.do")
    public ModelAndView findUserByIdAndAllRole(String id) throws Exception {

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("user-role-add");
        List<Role> list=userService.findUserByIdAndAllRole(id);
        modelAndView.addObject("roleList",list);
        modelAndView.addObject("userId",id);
        return modelAndView;

    }

    @RequestMapping(value="/addRoleToUser.do")
    public String addRoleToUser(@RequestParam(name = "userId",required = true) String userId,@RequestParam(name = "ids",required = true) String []ids) throws Exception {

        userService.addRoleToUser(userId,ids);

        return "redirect:findAll.do";

    }
}
