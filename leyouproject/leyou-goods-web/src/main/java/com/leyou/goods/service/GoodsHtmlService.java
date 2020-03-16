package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlService {

    @Autowired
    private TemplateEngine templateEngine;


    //把静态文件生成到本地
    public void createHtml(Map<String,Object> map,Long id) {

        //初始化运行上下文
        Context context = new Context();
        context.setVariables(map);
        PrintWriter printWriter = null;
        try {
        File file = new File("C:\\Users\\Hello\\Desktop\\item\\"+id+".html");

            printWriter = new PrintWriter(file);


        this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(printWriter!=null){
                printWriter.close();
            }
        }
    }
}
