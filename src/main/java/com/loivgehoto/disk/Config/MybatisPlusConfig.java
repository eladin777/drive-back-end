package com.loivgehoto.disk.Config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.loivgehoto.disk.Mapper")//////扫描mapper接口
public class MybatisPlusConfig
{


    //分页插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor()
    {
        MybatisPlusInterceptor mybatisPlusInterceptor=new MybatisPlusInterceptor();

        ////加载分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());


        return  mybatisPlusInterceptor;
    }
}
