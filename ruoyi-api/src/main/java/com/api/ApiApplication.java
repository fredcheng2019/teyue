package com.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.api.base.config.SystemProperties;



@SpringBootApplication
@EnableAsync          //启动异步线程注解，拦截器李不能使用异步线程注解
@EnableCaching
@EnableScheduling
@ServletComponentScan //配置druid必须加的注解，如果不加，访问页面打不开，filter和servlet、listener之类的需要单独进行注册才能使用，spring boot里面提供了该注解起到注册作用
@MapperScan(basePackages="com.api.bussiness.dao")
@EnableConfigurationProperties({SystemProperties.class})
@EnableHystrix
public class ApiApplication extends SpringBootServletInitializer{

	// 使spring boot 可以外部部署到tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ApiApplication.class);
		springApplication.run(args);
		System.out.println("Api接口系统启动成功");
	}
}
