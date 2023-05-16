package com.ruoyi.web.controller.merchant;

import com.ruoyi.basedata.domain.Bank;
import com.ruoyi.basedata.service.IBankService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.merchant.domain.MerchantBank;
import com.ruoyi.merchant.domain.MerchantBankRSP;
import com.ruoyi.merchant.service.IMerchantBankService;
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
@RequestMapping("/merchant/merchantBank")
public class MerchantBankController extends BaseController
{
    private String prefix = "merchant/merchantBank";

	@Autowired
	private IMerchantBankService merchantBankService;
	@Autowired
	private IBankService bankService;
	@Autowired
	private IMpMerchantService mpMerchantService;

	@GetMapping()
	public String merchantBank()
	{
	    return prefix + "/merchantBank";
	}

	/**
	 * 查询列表
	 */
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(MerchantBank merchantBank)
	{
		startPage();
        List<MerchantBankRSP> list = merchantBankService.selectMerchantBankList(merchantBank);
		return getDataTable(list);
	}


	/**
	 * 导出列表
	 */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MerchantBank merchantBank)
    {
    	List<MerchantBankRSP> list = merchantBankService.selectMerchantBankList(merchantBank);
        ExcelUtil<MerchantBankRSP> util = new ExcelUtil<MerchantBankRSP>(MerchantBankRSP.class);
        return util.exportExcel(list, "merchantBank");
    }

	/**
	 * 新增
	 */
	@GetMapping("/add/{mpMerchantId}")
	public String add(@PathVariable("mpMerchantId") Integer mpMerchantId, ModelMap mmap)
	{
		Bank bank = new Bank();
		mmap.put("banks",bankService.selectBankList(bank));
		mmap.put("merchant",mpMerchantService.selectMpMerchantById(mpMerchantId.longValue()));
	    return prefix + "/add";
	}

	/**
	 * 新增保存
	 */
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(MerchantBank merchantBank)
	{
		MerchantBank merchantBankExist = new MerchantBank();
		merchantBankExist.setMerchantId(merchantBank.getMerchantId());
		merchantBankExist.setBankId(merchantBank.getBankId());
		merchantBankExist.setBankBranch(merchantBank.getBankBranch());
		merchantBankExist.setBankSubBranch(merchantBank.getBankSubBranch());
		merchantBankExist.setBankAccountNo(merchantBank.getBankAccountNo());

		List<MerchantBankRSP> merchantBankList = merchantBankService.selectMerchantBankList(merchantBankExist);
		if (merchantBankList.size() >0) {
			return AjaxResult.error("当前商户已添加此卡号");
		}else {
            if(StringUtils.isBlank(merchantBank.getBankCity())){
                merchantBank.setBankCity("未知");
            }
			return toAjax(merchantBankService.insertMerchantBank(merchantBank));
		}
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		Bank bank = new Bank();
		mmap.put("banks",bankService.selectBankList(bank));
		MerchantBank merchantBank = merchantBankService.selectMerchantBankById(id);
		mmap.put("merchantBank", merchantBank);
	    return prefix + "/edit";
	}

	/**
	 * 修改保存
	 */
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(MerchantBank merchantBank)
	{
		MerchantBank merchantBankExist = new MerchantBank();
		merchantBankExist.setBankId(merchantBank.getBankId());
		merchantBankExist.setBankBranch(merchantBank.getBankBranch());
		merchantBankExist.setBankSubBranch(merchantBank.getBankSubBranch());
		merchantBankExist.setBankAccountNo(merchantBank.getBankAccountNo());
		List<MerchantBankRSP> merchantBankList = merchantBankService.selectMerchantBankList(merchantBankExist);
		if (merchantBankList.size() > 0) {
			MerchantBankRSP merchantBankRSP = merchantBankList.get(0);
			if (StringUtils.isNotNull(merchantBankExist) && merchantBankRSP.getId() != merchantBank.getId()) {
				return AjaxResult.error("已有银行已添加此卡号");
			}
		}
        if(StringUtils.isBlank(merchantBank.getBankCity())){
            merchantBank.setBankCity("未知");
        }
		return toAjax(merchantBankService.updateMerchantBank(merchantBank));
	}

	/**
	 * 删除
	 */
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{
		return toAjax(merchantBankService.deleteMerchantBankByIds(ids));
	}

}
