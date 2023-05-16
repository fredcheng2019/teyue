package com.ruoyi.web.controller.merchant;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.GoogleCodeUtil;
import com.ruoyi.common.utils.MD5;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import com.ruoyi.merchant.service.IMpMerchantService;

import com.ruoyi.system.service.ISysUserService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 信息操作处理
 *
 * @author ruoyi
 * @date 2019-05-06
 */
@Controller
@RequestMapping("/merchant/merchantInfo")
public class MpMerchantController extends BaseController {
    private String prefix = "merchant/mpMerchant";

    @Autowired
    private IMpMerchantService mpMerchantService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private MerchantService merchantService;


    @RequiresPermissions("merchant:merchantInfo:view")
    @GetMapping()
    public String mpMerchant() {
        return prefix + "/mpMerchant";
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("merchant:merchantInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MpMerchant mpMerchant) {
        //mpMerchant.setIsAgent(0);
        if (ShiroUtils.isAgent()) {
            // 如果是代理的话，查询的数据只能是该代理旗下的数据
            mpMerchant.setAgentId(ShiroUtils.getUserId());
        }
        startPage();
        List<MpMerchantRSP> list = mpMerchantService.selectMpMerchantList(mpMerchant);
        return getDataTable(list);
    }


    /**
     * 导出列表
     */
    @RequiresPermissions("merchant:merchantInfo:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MpMerchant mpMerchant) {
        if (ShiroUtils.isAgent()) {
            // 如果是代理的话，查询的数据只能是该代理旗下的数据
            mpMerchant.setAgentId(ShiroUtils.getUserId());
        }
        List<MpMerchantRSP> list = mpMerchantService.selectMpMerchantList(mpMerchant);
        ExcelUtil<MpMerchantRSP> util = new ExcelUtil<MpMerchantRSP>(MpMerchantRSP.class);
        return util.exportExcel(list, "mpMerchant");
    }

    /**
     * 新增
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存
     */
    @RequiresPermissions("merchant:merchantInfo:add")
    @Log(title = "商户信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MpMerchant mpMerchant) {
        return merchantService.addSave(mpMerchant);
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        MpMerchant mpMerchant = mpMerchantService.selectMpMerchantById(id);
        mmap.put("mpMerchant", mpMerchant);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     */
    @RequiresPermissions("merchant:merchantInfo:edit")
    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MpMerchant mpMerchant) {
        return mpMerchantService.updateMpMerchant(mpMerchant);
    }

    /**
     * 删除
     */
    @RequiresPermissions("merchant:merchantInfo:remove")
    @Log(title = "商户信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(mpMerchantService.deleteMpMerchantByIds(ids));
    }


    /**
     * 商户信息状态修改
     */
    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
    @RequiresPermissions("merchant:merchantInfo:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(MpMerchant mpMerchant) {
        return toAjax(mpMerchantService.changeStatus(mpMerchant));
    }

    /**
     * 商户信息状态修改
     */
    @Log(title = "商户信息", businessType = BusinessType.UPDATE)
    @RequiresPermissions("merchant:merchantInfo:edit")
    @PostMapping("/changeAdvanceStatus")
    @ResponseBody
    public AjaxResult changeAdvanceStatus(MpMerchant mpMerchant) {
        return toAjax(mpMerchantService.changeAdvanceStatus(mpMerchant));
    }

    /**
     * 商户名称校验
     *
     * @param mpMerchant
     * @return
     */
    @PostMapping("checkMerchantNameUnique")
    @ResponseBody
    public String checkMerchantNameUnique(MpMerchant mpMerchant) {
        return mpMerchantService.checkMerchantNameUnique(mpMerchant);
    }


    /**
     * 商户支付方法列表
     */
    @GetMapping("/payMethod/{mpMerchantId}")
    public String mpMerchantMethod(@PathVariable("mpMerchantId") Long mpMerchantId, ModelMap mmap) {
        mmap.put("mpMerchant", mpMerchantService.selectMpMerchantById(mpMerchantId));
        return "merchant/mpMerchantMethod/mpMerchantMethod";
    }

    /**
     * 商户代付设置
     */
//    @RequiresPermissions("merchant:withdrawalMethod:edit")
    @GetMapping("/withdrawalMethod/{mpMerchantId}")
    public String mpWithdrawalMethod(@PathVariable("mpMerchantId") Long mpMerchantId, ModelMap mmap) {
        MpMerchant mpMerchant = mpMerchantService.selectMpMerchantById(mpMerchantId);
        mmap.put("id", mpMerchantId);
        mmap.put("dfChannelId", mpMerchant.getDfChannelId());
        return "merchant/mpMerchant/withdrawalMethodSwithch";
    }

    /**
     * 商户代付设置
     */
//    @RequiresPermissions("merchant:withdrawalMethod:edit")
    @PostMapping("/withdrawalSwitchSetting")
    @ResponseBody
    public AjaxResult withdrawalSwitchSetting(Long dfChannelId, Long id) {
        if (dfChannelId == null) {
            return AjaxResult.error("请选择代付通道");
        }
        return mpMerchantService.updateMpMerchantDfChannelId(dfChannelId, id);
    }

    /**
     * 商户-ip黑白名单列表
     */
    @RequiresPermissions("merchant:merchantInfo:edit")
    @GetMapping("/ipList/{mpMerchantId}")
    public String mpChannelIp(@PathVariable("mpMerchantId") Long mpMerchantId, ModelMap mmap) {
        mmap.put("mpMerchant", mpMerchantService.selectMpMerchantById(mpMerchantId));
        return "merchant/merchantIp/merchantIp";
    }


    /**
     * 商户-收款银行列表
     */
    @RequiresPermissions("merchant:merchantInfo:edit")
    @GetMapping("/merchantBank/{mpMerchantId}")
    public String merchantBank(@PathVariable("mpMerchantId") Long mpMerchantId, ModelMap mmap) {
        mmap.put("mpMerchant", mpMerchantService.selectMpMerchantById(mpMerchantId));
        return "merchant/merchantBank/merchantBank";
    }


    /**
     * 重新获取Google+秘钥
     */
    @PostMapping("/resetCode/{type}")
    @ResponseBody
    public String resetCode(@PathVariable("type") String type) {
        if(type.equals("gg")){
            return GoogleCodeUtil.createSecretKey();
        }
        return MD5.MD5Encode(System.currentTimeMillis()+"").toLowerCase();
    }

    /**
     * 重新获取商户号
     */
    @PostMapping("/resetMerchantNo")
    @ResponseBody
    public String resetMerchantNo() {
       return mpMerchantService.resetMerchantNo();
    }


    /**
     * 商户支付方法-批量费率
     */
    @GetMapping("/methodStatus")
    public String methodStatus() {
        return prefix + "/merchantMethodStatus";
    }


    /**
     * 商户支付方法-批量开关
     */
    @GetMapping("/methodSwitch")
    public String methodSwitch() {
        return prefix + "/merchantMethodSwitch";
    }

    /**
     * 商户支付方法-批量支付限额
     */
    @GetMapping("/methodPayLimit")
    public String methodPayLimit() {
        return prefix + "/merchantMethodPayLimit";
    }


}
