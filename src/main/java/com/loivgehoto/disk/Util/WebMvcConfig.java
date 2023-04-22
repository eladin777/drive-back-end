package com.loivgehoto.disk.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {



    @Value("${platform.File_Path}")
    private String File_Path;



//    设置虚拟路径，右边为机器实际路径名,用于支持对图片等文件的浏览
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        if(System_Name.equals("windows"))//        windows下
//            registry.addResourceHandler("/rootFolder/**").addResourceLocations("file:D:\\Cloud_Disk\\");
//        else if (System_Name.equals("linux"))//        linux系统下
//            registry.addResourceHandler("/rootFolder/**").addResourceLocations("file:/Cloud_Disk/");
        registry.addResourceHandler("/rootFolder/**").addResourceLocations("file:"+File_Path);
    }
}
