package com.ruoyi.basedata.service.impl;

import com.ruoyi.basedata.domain.Bank;
import com.ruoyi.basedata.mapper.BankMapper;
import com.ruoyi.basedata.service.IBankService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 银行种类 服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
@Service
public class BankServiceImpl implements IBankService
{
	/** 银行名称是否唯一的返回结果 */
	public final static String BANK_NAME_UNIQUE = "0";
	public final static String BANK_NAME_NOT_UNIQUE = "1";

	@Autowired
	private BankMapper bankMapper;

	/**
     * 查询银行种类信息
     * 
     * @param id 银行种类ID
     * @return 银行种类信息
     */
    @Override
	public Bank selectBankById(Integer id)
	{
	    return bankMapper.selectBankById(id);
	}
	
	/**
     * 查询银行种类列表
     * 
     * @param bank 银行种类信息
     * @return 银行种类集合
     */
	@Override
	public List<Bank> selectBankList(Bank bank)
	{
	    return bankMapper.selectBankList(bank);
	}
	
    /**
     * 新增银行种类
     * 
     * @param bank 银行种类信息
     * @return 结果
     */
	@Override
	public int insertBank(Bank bank)
	{
	    return bankMapper.insertBank(bank);
	}
	
	/**
     * 修改银行种类
     * 
     * @param bank 银行种类信息
     * @return 结果
     */
	@Override
	public int updateBank(Bank bank)
	{
	    return bankMapper.updateBank(bank);
	}

	/**
     * 删除银行种类对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteBankByIds(String ids)
	{
		return bankMapper.deleteBankByIds(Convert.toStrArray(ids));
	}


	/**
	 * 银行状态修改
	 *
	 * @param bank 银行信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(Bank bank) {
		return bankMapper.updateBank(bank);
	}

	/**
	 * +
	 * 校验名字是否唯一
	 *
	 * @param bank 银行信息
	 * @return 结果
	 */
	@Override
	public String checkBankNameUnique(Bank bank) {
		Long bankId = StringUtils.isNull(bank.getId()) ? -1L : bank.getId();
		Bank info = bankMapper.checkBankNameUnique(bank.getName());
		if (StringUtils.isNotNull(info) && info.getId().longValue() != bankId.longValue()) {
			return BANK_NAME_NOT_UNIQUE;
		}
		return BANK_NAME_UNIQUE;
	}


	/**
	 * 查询不在bankIds里的银行
	 *
	 * @param bankIds 银行id集合
	 * @return 银行集合
	 */
	@Override
	public List<Bank> selectBankListWithoutBankIds(List<Integer> bankIds) {
		return bankMapper.selectBankListWithoutBankIds(bankIds);
	}
}
