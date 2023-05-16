package com.ruoyi.web.controller.financial;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.basedata.domain.Bank;
import com.ruoyi.basedata.domain.ConfirmResult;
import com.ruoyi.basedata.service.IBankService;

import com.ruoyi.common.utils.GoogleCodeUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.financial.domain.FinancialWalletRSP;
import com.ruoyi.financial.domain.MpFinancialSetting;
import com.ruoyi.financial.service.IMpFinancialSettingService;
import com.ruoyi.merchant.domain.HttpReqPlatformWithdrew;
import com.ruoyi.merchant.domain.MerchantWithdrawalPay;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.financial.domain.FinancialWallet;
import com.ruoyi.financial.service.IFinancialWalletService;
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
@RequestMapping("/financial/financialWallet")
public class FinancialWalletController extends BaseController
{
	Logger log = LoggerFactory.getLogger(FinancialWalletController.class);
    private String prefix = "financial/financialWallet";
	
	@Autowired
	private IFinancialWalletService financialWalletService;
	@Autowired
	private IBankService bankService;
	@Autowired
	private IMpFinancialSettingService financialSettingService;
	@Value("${reqUrlConfig.managerFinanceWithdrawal}")
	private String managerFinanceWithdrawal;
	
	@RequiresPermissions("financial:financialWallet:view")
	@GetMapping()
	public String financialWallet()
	{
	    return prefix + "/financialWallet";
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("financial:financialWallet:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(FinancialWallet financialWallet, String channelName)
	{
		return null;
	}
	
	
	/**
	 * 导出列表
	 */
	@RequiresPermissions("financial:financialWallet:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinancialWallet financialWallet)
    {
    	List<FinancialWalletRSP> list = financialWalletService.selectFinancialWalletList(financialWallet);
        ExcelUtil<FinancialWalletRSP> util = new ExcelUtil<FinancialWalletRSP>(FinancialWalletRSP.class);
        return util.exportExcel(list, "financialWallet");
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
	@RequiresPermissions("financial:financialWallet:add")
	@Log(title = "", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(FinancialWallet financialWallet)
	{		
		return toAjax(financialWalletService.insertFinancialWallet(financialWallet));
	}

	/**
	 * 修改
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap)
	{
		FinancialWallet financialWallet = financialWalletService.selectFinancialWalletById(id);
		mmap.put("financialWallet", financialWallet);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存
	 */
	@RequiresPermissions("financial:financialWallet:edit")
	@Log(title = "", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(FinancialWallet financialWallet)
	{		
		return toAjax(financialWalletService.updateFinancialWallet(financialWallet));
	}
	
	/**
	 * 删除
	 */
	@RequiresPermissions("financial:financialWallet:remove")
	@Log(title = "", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(financialWalletService.deleteFinancialWalletByIds(ids));
	}


	/**
	 * 平台代付
	 */
	@GetMapping("/withdrawal")
	public String withdrawal() {
		return prefix + "/platformWithdrawal";
	}


	/**
	 * 平台代付（平台代付自己的钱包）
	 */
	@PostMapping("/withdrawalPay")
	@ResponseBody
	public AjaxResult withdrawalPay(MerchantWithdrawalPay merchantWithdrawalPay) {
		try{
			MpFinancialSetting setting = new MpFinancialSetting();
			List<MpFinancialSetting> mpFinancialSettings = financialSettingService.selectMpFinancialSettingList(setting);
			MpFinancialSetting mpFinancialSetting = mpFinancialSettings.get(0);
			if (StringUtils.isNotNull(mpFinancialSetting)) {
				// 取款密码
				boolean password = merchantWithdrawalPay.getWithdrawalPassword().equals(mpFinancialSetting.getWithdrawalPassword());
				// 谷歌验证
				boolean googleCode = GoogleCodeUtil.ValidCode(merchantWithdrawalPay.getGoogleCode(), mpFinancialSetting.getGoogleSecret());
				if (!password) {
					return AjaxResult.error("取款密码错误！");
				}
				if (!googleCode) {
					return AjaxResult.error("验证码验证失败！");
				}
				if (password && googleCode) {
					// 封装请求参数
					HttpReqPlatformWithdrew platformWithdrew = makeReqParams(merchantWithdrawalPay);
					String msg = JSONObject.fromObject(platformWithdrew).toString();
					String result = HttpUtils.sendPost(managerFinanceWithdrawal, msg, false);
					if (StringUtils.isNotEmpty(result)) {
						JSONObject jsonObjectResult = JSONObject.fromObject(result);
						ConfirmResult confirmResult = (ConfirmResult) net.sf.json.JSONObject.toBean(jsonObjectResult, ConfirmResult.class);
						if (confirmResult.getStatus() == 0) {
							return AjaxResult.success(confirmResult.getMsg());
						}else {
							return AjaxResult.error(confirmResult.getMsg());
						}
					}else {
						return AjaxResult.error("请求失败!");
					}
				}
			}
			return AjaxResult.error("没有进行账户相关设置");
		}catch (Exception e){
			e.printStackTrace();
			return AjaxResult.error("系统异常");
		}
	}


	/**
	 * 封装代付请求参数
	 */
	public HttpReqPlatformWithdrew makeReqParams(MerchantWithdrawalPay merchantWithdrawalPay) {
		HttpReqPlatformWithdrew platformWithdrew = new HttpReqPlatformWithdrew();
		// 交易金额
		platformWithdrew.setTotal_fee(merchantWithdrawalPay.getAccountBalance());
		// 渠道id
		platformWithdrew.setChannel_id(merchantWithdrawalPay.getChannelId());
		// 查询银行
		Bank bank = bankService.selectBankById(merchantWithdrawalPay.getBankId().intValue());
		if (StringUtils.isNotNull(bank)) {
			// 银行名称
			platformWithdrew.setBank_name(bank.getName());
			// 银行编码
			platformWithdrew.setBank_code(bank.getCode());
		}
		// 银行分行
		platformWithdrew.setBank_branch(merchantWithdrawalPay.getBankBranch());
		// 银行支行
		platformWithdrew.setBank_sub_branch(merchantWithdrawalPay.getBankSubBranch());
		// 所在省
		platformWithdrew.setBank_province(merchantWithdrawalPay.getBankProvince());
		// 所在市
		platformWithdrew.setBank_city(merchantWithdrawalPay.getBankCity());
		// 联行号
		platformWithdrew.setBank_union_no(merchantWithdrawalPay.getBankUnionNo());
		// 持卡人
		platformWithdrew.setBank_account_owner(merchantWithdrawalPay.getBankAccountOwner());
		// 卡号
		platformWithdrew.setBank_account_no(merchantWithdrawalPay.getBankAccountNo());
		// 账户类型
		platformWithdrew.setBank_account_type(merchantWithdrawalPay.getBankAccountType());

		return platformWithdrew;
	}
}
