package com.api.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.api.interceptor.ManagerInterceptor;
import com.api.interceptor.MerchantInterceptor;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
//    以下WebMvcConfigurerAdapter 比较常用的重写接口
//    /** 解决跨域问题 **/
//    public void addCorsMappings(CorsRegistry registry) ;
//    /** 添加拦截器 **/
//    void addInterceptors(InterceptorRegistry registry);
//    /** 这里配置视图解析器 **/
//    void configureViewResolvers(ViewResolverRegistry registry);
//    /** 配置内容裁决的一些选项 **/
//    void configureContentNegotiation(ContentNegotiationConfigurer configurer);
//    /** 视图跳转控制器 **/
//    void addViewControllers(ViewControllerRegistry registry);
//    /** 静态资源处理 **/
//    void addResourceHandlers(ResourceHandlerRegistry registry);
//    /** 默认静态资源处理器 **/
//    void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer);

    @Autowired
    private MerchantInterceptor merchantInterceptor;

    @Autowired
    private ManagerInterceptor managerInterceptor;

    /**
     *
     * 功能描述:
     *  配置静态资源,避免静态资源请求被拦截
     * @auther: Tt(yehuawei)
     * @date:
     * @param:
     * @return:
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");

        registry.addResourceHandler("/webapp/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/webapp/");
        super.addResourceHandlers(registry);
    }

    public void addInterceptors(InterceptorRegistry registry) {
    	//商户平台拦截器
        registry.addInterceptor(merchantInterceptor)
                //addPathPatterns 用于添加拦截规则
                .addPathPatterns("/gateway/**")
                .addPathPatterns("/pay/**")
                //excludePathPatterns 用于排除拦截
                //注意content-path：ding是不用填写的
                //回调不拦截
                .excludePathPatterns("/notify/**");

        //管理平台拦截器
        registry.addInterceptor(managerInterceptor)
		        //addPathPatterns 用于添加拦截规则
		        .addPathPatterns("/manager/**");

        super.addInterceptors(registry);
    }
}
