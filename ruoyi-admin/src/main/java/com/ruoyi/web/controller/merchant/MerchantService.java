package com.ruoyi.web.controller.merchant;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.GoogleCodeUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.service.IMpMerchantService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @auther Lengff
 * @time 2020/9/15
 * @fileName MerchantService
 */
@Service
public class MerchantService {
    @Autowired
    private IMpMerchantService mpMerchantService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private ISysUserService userService;

    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSave(MpMerchant mpMerchant) {
        if (ShiroUtils.isAgent()) {
            // 如果是代理的话，为商户绑定代理ID
            mpMerchant.setAgentId(ShiroUtils.getUserId());
        }
        SysUser sysUser = userService.selectUserByPhoneNumber(mpMerchant.getContactPhone());
        if (sysUser != null) {
            return AjaxResult.error("该手机号已经被其他商户注册");
        }
        if (StringUtils.isNoneBlank(mpMerchant.getContactEmail())) {
            sysUser = userService.selectUserByEmail(mpMerchant.getContactEmail());
            if (sysUser != null) {
                return AjaxResult.error("该邮箱已经被其他商户注册");
            }
        }
        sysUser = userService.selectUserByLoginName(mpMerchant.getName());
        if (sysUser != null) {
            return AjaxResult.error("该用户名已经被其他商户注册");
        }
        mpMerchant.setGoogleSecret(GoogleCodeUtil.createSecretKey());
        SysUser user = new SysUser();
        user.setGoogleSecret(mpMerchant.getGoogleSecret());
        user.setDeptId(112L);
        user.setUserName(mpMerchant.getName());
        user.setPhonenumber(mpMerchant.getContactPhone());
        user.setEmail(StringUtils.isBlank(mpMerchant.getContactEmail()) ? null : mpMerchant.getContactEmail());
        user.setLoginName(mpMerchant.getName());
        user.setGoogleSecret(mpMerchant.getGoogleSecret());
        user.setPassword("123321");
        user.setSex("0");
        user.setRoleId(6L);
        user.setRemark("新增商户时自动注册的用户信息");
        user.setStatus("0");
        Long[] roleIds = {6L};
        Long[] postIds = {4L};
        user.setRoleIds(roleIds);
        user.setPostIds(postIds);
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        if (userService.insertUser(user) > 0) {
            // 谷歌验证秘钥
            mpMerchant.setUserId(user.getUserId().longValue());
            return mpMerchantService.insertMpMerchant(mpMerchant);
        }
        return AjaxResult.error("添加商户信息失败");
    }
}
