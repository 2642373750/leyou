package com.zs.ssm.controller;

import com.zs.ssm.pojo.SysLog;
import com.zs.ssm.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Component
@Aspect
public class LogAop {

    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;
    private Date startTime;
    private Class clazz;
    private Method method;

    @Before("execution(* com.zs.ssm.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        startTime=new Date();
        //获得具体访问的类
        clazz=jp.getTarget().getClass();
        //获取方法名
        String methodName=jp.getSignature().getName();
        //获取访问方法参数
        Object[] obj=jp.getArgs();
        //通过方法名获取方法
        if(obj==null||obj.length==0) {
            method = clazz.getMethod(methodName);
        }else{
            Class []classArgs=new Class[obj.length];
            for(int i=0;i<obj.length;i++){
                classArgs[i]=obj[i].getClass();
            }
            clazz.getMethod(methodName,classArgs);
        }
    }

    @After("execution(* com.zs.ssm.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {

        long time=new Date().getTime()-startTime.getTime();

        //获取url
        String url="";
        if(clazz!=null&&method!=null&&clazz!=LogAop.class){
            //获取类上的RequestMapper
            RequestMapping classAnnotation= (RequestMapping) clazz.getAnnotation(RequestMapping.class);
            if(classAnnotation!=null) {
                String []classValue=classAnnotation.value();
                //获取方法上的RequestMapper
                RequestMapping methodAnnotation=method.getAnnotation(RequestMapping.class);
                if(methodAnnotation!=null){
                    String[] methodValue=methodAnnotation.value();
                    url=classValue[0]+methodValue[0];
                    //获取ip
                    String ip=request.getRemoteAddr();

                    //获取当前操作用户
                    SecurityContext context= SecurityContextHolder.getContext();
                    User user= (User) context.getAuthentication().getPrincipal();
                    String username=user.getUsername();

                    //封装日志
                    SysLog sysLog=new SysLog();
                    sysLog.setExecutionTime(time);
                    sysLog.setIp(ip);
                    sysLog.setMethod("[类名]"+clazz.getName()+"[方法名]"+method.getName());
                    sysLog.setUrl(url);
                    sysLog.setUsername(username);
                    sysLog.setVisitTime(startTime);
                    sysLogService.save(sysLog);
                }
            }

        }


    }
}
