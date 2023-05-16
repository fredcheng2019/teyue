package com.ruoyi.web.controller.merchantAccount;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import com.ruoyi.merchant.domain.MpMerchantUpdateReq;
import com.ruoyi.merchant.service.IMpMerchantService;
import com.ruoyi.merchantAccount.domain.MerchantSetting;
import com.ruoyi.merchantAccount.service.IMerchantSettingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/merchantAccount/setting")
public class MerchantSettingController extends BaseController {

    private String prefix = "merchantAccount/mpMerchantSetting";

    @Autowired
    private IMerchantSettingService merchantSettingService;
    @Autowired
    private IMpMerchantService mpMerchantService;

    @RequiresPermissions("merchantAccount:mpMerchantSetting:view")
    @GetMapping()
    public String mpMerchant(ModelMap mmap) {
        MpMerchant mpMerchant = new MpMerchant();
        mpMerchant.setUserId(ShiroUtils.getUserId().longValue());
        List<MpMerchantRSP> mpMerchantRSPS = mpMerchantService.selectMpMerchantList(mpMerchant);
        if (mpMerchantRSPS.size() > 0) {
            MpMerchantRSP mpMerchantRSP = mpMerchantRSPS.get(0);
            mmap.put("username", mpMerchantRSP.getCode());
        }
        return prefix + "/mpMerchantSetting";
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("merchantAccount:mpMerchantSetting:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MerchantSetting merchantSetting) {
        MpMerchant mpMerchant = new MpMerchant();
        mpMerchant.setUserId(ShiroUtils.getUserId().longValue());
        List<MpMerchantRSP> mpMerchantRSPS = mpMerchantService.selectMpMerchantList(mpMerchant);
        if (mpMerchantRSPS.size() > 0) {
            MpMerchantRSP mpMerchantRSP = mpMerchantRSPS.get(0);
            merchantSetting.setId(mpMerchantRSP.getId());
        } else {
            merchantSetting.setId(0L);
        }
        startPage();
        List<MerchantSetting> list = merchantSettingService.selectMpMerchantList(merchantSetting);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @RequiresPermissions("merchantAccount:mpMerchantSetting:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MerchantSetting merchantSetting) {
        MpMerchant mpMerchant = new MpMerchant();
        mpMerchant.setUserId(ShiroUtils.getUserId().longValue());
        List<MpMerchantRSP> mpMerchantRSPS = mpMerchantService.selectMpMerchantList(mpMerchant);
        if (mpMerchantRSPS.size() > 0) {
            MpMerchantRSP mpMerchantRSP = mpMerchantRSPS.get(0);
            merchantSetting.setId(mpMerchantRSP.getId());
        } else {
            merchantSetting.setId(0L);
        }
        List<MerchantSetting> list = merchantSettingService.selectMpMerchantList(merchantSetting);
        ExcelUtil<MerchantSetting> util = new ExcelUtil<MerchantSetting>(MerchantSetting.class);
        return util.exportExcel(list, "merchantSetting");
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        MerchantSetting merchantSetting = merchantSettingService.selectMpMerchantById(id);
        mmap.put("merchantSetting", merchantSetting);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MpMerchantUpdateReq merchantSetting) {
        merchantSetting.setUserId(ShiroUtils.getUserId());
        return toAjax(merchantSettingService.updateMpMerchant(merchantSetting));
    }
}
