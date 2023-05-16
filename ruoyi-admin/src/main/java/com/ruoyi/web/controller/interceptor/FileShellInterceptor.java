package com.ruoyi.web.controller.interceptor;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

public class FileShellInterceptor implements HandlerInterceptor {

    // 文件后缀白名单
    private static String suffixList ="doc,docx,eml,htm,html,jpg,mht,msg,png,ppt,pptx,rar,txt,xls,xlsx,zip,pdf,jpeg,gif";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest req=(HttpServletRequest)request;
        MultipartResolver multipartResolver=new CommonsMultipartResolver();
        if(multipartResolver.isMultipart(req)){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> files= multipartRequest.getFileMap();
            Iterator<String> iterator = files.keySet().iterator();
            while(iterator.hasNext()){
                String formKey = (String) iterator.next();
                MultipartFile multipartFile = multipartRequest.getFile(formKey);
                if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
                    String filename = multipartFile.getOriginalFilename();
                    if(checkFile(filename)){
                        return true;
                    } else {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("text/html");
                        response.getWriter().write("<script>alert('上传文件无效！');</script>");
                        return false;
                    }
                }
            }
            return true;
        }else{
            return true;
        }
    }
    private  boolean checkFile(String fileName){
        boolean flag=false;
        //获取文件后缀
        String suffix=fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());

        if(suffixList.contains(suffix.trim().toLowerCase())){
            flag=true;
        }
        return flag;
    }
}
