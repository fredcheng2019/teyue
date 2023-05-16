package com.ruoyi.web.controller.financial;

import java.util.List;

import com.ruoyi.basedata.domain.Bank;
import com.ruoyi.basedata.service.IBankService;
import com.ruoyi.financial.domain.FinancialBankRSP;
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
import com.ruoyi.financial.domain.FinancialBank;
import com.ruoyi.financial.service.IFinancialBankService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 *  信息操作处理
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
@Controller
@RequestMapping("/financial/financialBank")
public class FinancialBankController extends BaseController
{
    private String prefix = "financial/financialBank";
	
	@Autowired
	private IFinancialBankService financialBankService;
	@Autowired
	private IBankService bankService;
	
	@RequiresPermissions("financial:financialBank:view")
	@GetMapping()
	public String financialBank()
	{
	    return prefix + "/financialBank";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("financial:financialBank:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(FinancialBank financialBank)
	{
		startPage();
        List<FinancialBankRSP> list = financialBankService.selectFinancialBankList(financialBank);
		return getDataTable(list);
	}
	
	
	/**
	 * 导出列表
	 */
	@RequiresPermissions("financial:financialBank:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinancialBank financialBank)
    {
    	List<FinancialBankRSP> list = financialBankService.selectFinancialBankList(financialBank);
        ExcelUtil<FinancialBankRSP> util = new ExcelUtil<FinancialBankRSP>(FinancialBankRSP.class);
        return util.exportExcel(list, "financialBank");
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
	@RequiresPermissions("financial:financialBank:add")
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(FinancialBank financialBank)
	{		
		return toAjax(financialBankService.insertFinancialBank(financialBank));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		Bank bank = new Bank();
		mmap.put("banks",bankService.selectBankList(bank));
		FinancialBank financialBank = financialBankService.selectFinancialBankById(id);
		mmap.put("financialBank", financialBank);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存
	 */
	@RequiresPermissions("financial:financialBank:edit")
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(FinancialBank financialBank)
	{		
		return toAjax(financialBankService.updateFinancialBank(financialBank));
	}
	
	/**
	 * 删除
	 */
	@RequiresPermissions("financial:financialBank:remove")
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(financialBankService.deleteFinancialBankByIds(ids));
	}
	
}
