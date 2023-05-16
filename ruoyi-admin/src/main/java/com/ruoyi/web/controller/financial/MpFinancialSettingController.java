package com.ruoyi.web.controller.financial;

import java.util.List;

import com.ruoyi.common.utils.GoogleCodeUtil;
import com.ruoyi.common.utils.MD5;
import com.ruoyi.merchant.service.IMpMerchantService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.financial.domain.MpFinancialSetting;
import com.ruoyi.financial.service.IMpFinancialSettingService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 *  信息操作处理
 * 
 * @author ruoyi
 * @date 2019-05-13
 */
@Controller
@RequestMapping("/financial/mpFinancialSetting")
public class MpFinancialSettingController extends BaseController
{
    private String prefix = "financial/mpFinancialSetting";
	
	@Autowired
	private IMpFinancialSettingService mpFinancialSettingService;

	@Autowired
	private IMpMerchantService mpMerchantService;
	
	@RequiresPermissions("financial:mpFinancialSetting:view")
	@GetMapping()
	public String mpFinancialSetting()
	{
	    return prefix + "/mpFinancialSetting";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("financial:mpFinancialSetting:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MpFinancialSetting mpFinancialSetting)
	{
		startPage();
        List<MpFinancialSetting> list = mpFinancialSettingService.selectMpFinancialSettingList(mpFinancialSetting);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出列表
	 */
	@RequiresPermissions("financial:mpFinancialSetting:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MpFinancialSetting mpFinancialSetting)
    {
    	List<MpFinancialSetting> list = mpFinancialSettingService.selectMpFinancialSettingList(mpFinancialSetting);
        ExcelUtil<MpFinancialSetting> util = new ExcelUtil<MpFinancialSetting>(MpFinancialSetting.class);
        return util.exportExcel(list, "mpFinancialSetting");
    }
	
	/**
	 * 新增
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存
	 */
	@RequiresPermissions("financial:mpFinancialSetting:add")
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MpFinancialSetting mpFinancialSetting)
	{		
		return toAjax(mpFinancialSettingService.insertMpFinancialSetting(mpFinancialSetting));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		MpFinancialSetting mpFinancialSetting = mpFinancialSettingService.selectMpFinancialSettingById(id);
		mmap.put("mpFinancialSetting", mpFinancialSetting);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存
	 */
	@RequiresPermissions("financial:mpFinancialSetting:edit")
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MpFinancialSetting mpFinancialSetting)
	{		
		return toAjax(mpFinancialSettingService.updateMpFinancialSetting(mpFinancialSetting));
	}
	
	/**
	 * 删除
	 */
	@RequiresPermissions("financial:mpFinancialSetting:remove")
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(mpFinancialSettingService.deleteMpFinancialSettingByIds(ids));
	}



	/**
	 * 内扣状态修改
	 */
	@PostMapping("/changeInnerStatus")
	@ResponseBody
	public AjaxResult changeInnerStatus(MpFinancialSetting mpFinancialSetting) {
		return  toAjax(mpFinancialSettingService.changeInnerStatus(mpFinancialSetting));
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
}
