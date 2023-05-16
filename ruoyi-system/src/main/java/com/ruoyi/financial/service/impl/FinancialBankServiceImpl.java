package com.ruoyi.financial.service.impl;

import java.util.List;

import com.ruoyi.financial.domain.FinancialBankRSP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.financial.mapper.FinancialBankMapper;
import com.ruoyi.financial.domain.FinancialBank;
import com.ruoyi.financial.service.IFinancialBankService;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
@Service
public class FinancialBankServiceImpl implements IFinancialBankService 
{
	@Autowired
	private FinancialBankMapper financialBankMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public FinancialBank selectFinancialBankById(Long id)
	{
	    return financialBankMapper.selectFinancialBankById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param financialBank 信息
     * @return 集合
     */
	@Override
	public List<FinancialBankRSP> selectFinancialBankList(FinancialBank financialBank)
	{
	    return financialBankMapper.selectFinancialBankList(financialBank);
	}
	
    /**
     * 新增
     * 
     * @param financialBank 信息
     * @return 结果
     */
	@Override
	public int insertFinancialBank(FinancialBank financialBank)
	{
	    return financialBankMapper.insertFinancialBank(financialBank);
	}
	
	/**
     * 修改
     * 
     * @param financialBank 信息
     * @return 结果
     */
	@Override
	public int updateFinancialBank(FinancialBank financialBank)
	{
	    return financialBankMapper.updateFinancialBank(financialBank);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteFinancialBankByIds(String ids)
	{
		return financialBankMapper.deleteFinancialBankByIds(Convert.toStrArray(ids));
	}
	
}
