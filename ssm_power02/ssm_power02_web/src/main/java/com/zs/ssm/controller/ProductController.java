package com.zs.ssm.controller;

import com.zs.ssm.pojo.Product;
import com.zs.ssm.service.OrdersService;
import com.zs.ssm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value="/findAll.do")
    public ModelAndView findAll() throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        List<Product> list=productService.findAll();

        modelAndView.setViewName("product-list");
        modelAndView.addObject("productList",list);
        return modelAndView;
    }

    @RequestMapping(value="/save.do")
    public String save(Product product) throws Exception {

        productService.saveProduct(product);
        return "redirect:findAll.do";
    }
}
