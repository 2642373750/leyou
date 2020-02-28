package com.zs.ssm.controller;

import com.github.pagehelper.PageInfo;
import com.zs.ssm.pojo.Orders;
import com.zs.ssm.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping(value="/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


    @RequestMapping("/findAll.do")
    public ModelAndView findAll(@RequestParam(name="page",required = true)Integer page, @RequestParam(name="pageSize",required = true) Integer size) throws Exception {

            ModelAndView modelAndView = new ModelAndView();
            List<Orders> list = ordersService.findAll(page, size);
            modelAndView.setViewName("orders-page-list");
            PageInfo pageInfo = new PageInfo(list);
            modelAndView.addObject("pageInfo", pageInfo);
            return modelAndView;

    }

        @RequestMapping("/findById.do")
        public ModelAndView findById (@RequestParam(name = "id", required = true, defaultValue = "1") String id) throws Exception {
            ModelAndView modelAndView = new ModelAndView();
            Orders orders = ordersService.findById(id);
            modelAndView.addObject("orders", orders);
            modelAndView.setViewName("orders-show");
            return modelAndView;
        }

}
