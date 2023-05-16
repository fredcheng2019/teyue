package com.ruoyi.web.controller.merchant;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.merchant.domain.MerchantIp;
import com.ruoyi.merchant.domain.MerchantIpRSP;
import com.ruoyi.merchant.service.IMerchantIpService;
import com.ruoyi.merchant.service.IMpMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  信息操作处理
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
@Controller
@RequestMapping("/merchant/merchantIp")
public class MerchantIpController extends BaseController
{
    private String prefix = "merchant/merchantIp";
	
	@Autowired
	private IMerchantIpService merchantIpService;
	@Autowired
	private IMpMerchantService mpMerchantService;
	
	@GetMapping()
	public String merchantIp()
	{
	    return prefix + "/merchantIp";
	}
	
	/**
	 * 查询列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MerchantIp merchantIp)
	{
		startPage();
        List<MerchantIpRSP> list = merchantIpService.selectMerchantIpList(merchantIp);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出列表
	 */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MerchantIp merchantIp)
    {
    	List<MerchantIpRSP> list = merchantIpService.selectMerchantIpList(merchantIp);
        ExcelUtil<MerchantIpRSP> util = new ExcelUtil<MerchantIpRSP>(MerchantIpRSP.class);
        return util.exportExcel(list, "merchantIp");
    }
	
	/**
	 * 新增
	 */
	@GetMapping("/add/{merchantId}")
	public String add(@PathVariable("merchantId") Long merchantId, ModelMap mmap)
	{
		mmap.put("merchant",mpMerchantService.selectMpMerchantById(merchantId));
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存
	 */
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MerchantIp merchantIp)
	{		
		return toAjax(merchantIpService.insertMerchantIp(merchantIp));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		MerchantIp merchantIp = merchantIpService.selectMerchantIpById(id);
		mmap.put("merchantIp", merchantIp);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存
	 */
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MerchantIp merchantIp)
	{		
		return toAjax(merchantIpService.updateMerchantIp(merchantIp));
	}
	
	/**
	 * 删除
	 */
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(merchantIpService.deleteMerchantIpByIds(ids));
	}


	/**
	 * 	校验该商户是否添加该IP
	 */
	@PostMapping("/checkMerchantIpUnique")
	@ResponseBody
	public String checkIpUnique(MerchantIp merchantIp) {
		return merchantIpService.checkMerchantIpUnique(merchantIp);
	}


}
