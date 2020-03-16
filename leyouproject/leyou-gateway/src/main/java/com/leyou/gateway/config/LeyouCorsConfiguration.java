package com.leyou.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LeyouCorsConfiguration {

    @Bean
    public CorsFilter corsFilter(){

        //初始化cors配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
        corsConfiguration.addAllowedOrigin("http://www.leyou.com");
        corsConfiguration.setAllowCredentials(true);  //允许携带cookie
        corsConfiguration.addAllowedMethod("*");   //*代表所有请求
        corsConfiguration.addAllowedHeader("*");   //*允许携带任何头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource=new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        //返回cors实例，参数cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }
}
