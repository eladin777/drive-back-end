package com.loivgehoto.disk;

import com.github.pagehelper.PageHelper;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Properties;

@SpringBootApplication
public class DiskApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(DiskApplication.class, args);
	}

	//-----↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓解决springboot项目请求出现非法字符问题 java.lang.IllegalArgumentException:Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 3986(下载的文件的名称问题,download请求)
	@Bean
	public TomcatServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers((Connector connector) -> {
			connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
			connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
		});
		return factory;
	}
	//-----↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑



	@Bean
	public PageHelper pageHelper() {
		System.out.println("MyBatisConfiguration.pageHelper()");
		PageHelper pageHelper = new PageHelper();
		Properties p = new Properties();
		p.setProperty("offsetAsPageNum", "true");
		p.setProperty("rowBoundsWithCount", "true");
		p.setProperty("reasonable", "true");
		pageHelper.setProperties(p);
		return pageHelper;
	}


}
