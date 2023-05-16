package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import com.ruoyi.merchant.service.IMpMerchantService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysMenu;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysMenuService;

/**
 * 首页 业务处理
 *
 * @author ruoyi
 */
@Controller
public class SysIndexController extends BaseController {
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private IMpMerchantService mpMerchantService;
    @Autowired
    private ISysUserService userService;
    // 系统首页
    @GetMapping("/index")
    public String index(ModelMap mmap) {
        // 取身份信息
        SysUser user = ShiroUtils.getSysUser();
        // 根据用户id取出菜单
        List<SysMenu> menus = menuService.selectMenusByUser(user);

        //是商户
        MpMerchant mpMerchant = new MpMerchant();
        mpMerchant.setUserId(ShiroUtils.getUserId().longValue());
        List<MpMerchantRSP> mpMerchantRSPS = mpMerchantService.selectMpMerchantList(mpMerchant);
        if (!mpMerchantRSPS.isEmpty()) {
            MpMerchantRSP mpMerchantRSP = mpMerchantRSPS.get(0);
            //预付未开启
            if(mpMerchantRSP.getOpenAdvance()==null || mpMerchantRSP.getOpenAdvance()==1){
                for(SysMenu menu : menus){
                    if(menu.getMenuName().contains("预付")){
                        menus.remove(menu);
                    }
                    List<SysMenu> children = menu.getChildren();
                    if(children==null || children.isEmpty()){
                        continue;
                    }
                    for(SysMenu cmenu : children){
                        if(cmenu.getMenuName().contains("预付")){
                            children.remove(cmenu);
                            break;
                        }
                    }
                    menu.setChildren(children);
                }
            }
        }
        mmap.put("menus", menus);
        mmap.put("user", user);
        mmap.put("copyrightYear", Global.getCopyrightYear());
        return "index";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        mmap.put("version", Global.getVersion());
        SysUser user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
        MpMerchant mpMerchant = mpMerchantService.selectMpMerchantByUserId(user.getUserId());
        if(mpMerchant!=null){
            mmap.put("code", mpMerchant.getCode());
            mmap.put("secretKey", mpMerchant.getSecretKey());
            mmap.put("googleSecret", mpMerchant.getGoogleSecret());
            mmap.put("withdrawalPassword", mpMerchant.getWithdrawalPassword());
        }
        return "main";
    }
}
