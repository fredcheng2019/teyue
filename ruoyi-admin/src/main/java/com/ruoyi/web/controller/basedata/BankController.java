package com.ruoyi.web.controller.basedata;


import com.ruoyi.basedata.domain.Bank;

import com.ruoyi.basedata.service.IBankService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 银行种类 信息操作处理
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
@Controller
@RequestMapping("/basedata/bank")
public class BankController extends BaseController
{
    private String prefix = "basedata/bank";
	
	@Autowired
	private IBankService bankService;
	
	@RequiresPermissions("basedata:bank:view")
	@GetMapping()
	public String bank()
	{
	    return prefix + "/bank";
	}
	
	/**
	 * 查询银行种类列表
	 */
	@RequiresPermissions("basedata:bank:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Bank bank)
	{
		startPage();
        List<Bank> list = bankService.selectBankList(bank);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出银行种类列表
	 */
	@RequiresPermissions("basedata:bank:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Bank bank)
    {
    	List<Bank> list = bankService.selectBankList(bank);
        ExcelUtil<Bank> util = new ExcelUtil<Bank>(Bank.class);
        return util.exportExcel(list, "bank");
    }
	
	/**
	 * 新增银行种类
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存银行种类
	 */
	@RequiresPermissions("basedata:bank:add")
	@Log(title = "银行种类", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Bank bank)
	{		
		return toAjax(bankService.insertBank(bank));
	}

	/**
	 * 修改银行种类
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Bank bank = bankService.selectBankById(id);
		mmap.put("bank", bank);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存银行种类
	 */
	@RequiresPermissions("basedata:bank:edit")
	@Log(title = "银行种类", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Bank bank)
	{		
		return toAjax(bankService.updateBank(bank));
	}
	
	/**
	 * 删除银行种类
	 */
	@RequiresPermissions("basedata:bank:remove")
	@Log(title = "银行种类", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(bankService.deleteBankByIds(ids));
	}

	/**
	 * 银行状态修改
	 */
	@Log(title = "银行种类", businessType = BusinessType.UPDATE)
	@RequiresPermissions("basedata:bank:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(Bank bank) {
		return toAjax(bankService.changeStatus(bank));
	}


	/**
	 * 校验名字
	 */
	@PostMapping("/checkBankNameUnique")
	@ResponseBody
	public String checkBankNameUnique(Bank bank) {
		return bankService.checkBankNameUnique(bank);
	}

}
