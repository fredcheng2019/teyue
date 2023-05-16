package com.ruoyi.financial.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.financial.domain.FinancialWalletRSP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.financial.mapper.FinancialWalletMapper;
import com.ruoyi.financial.domain.FinancialWallet;
import com.ruoyi.financial.service.IFinancialWalletService;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
@Service
public class FinancialWalletServiceImpl implements IFinancialWalletService 
{
	@Autowired
	private FinancialWalletMapper financialWalletMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public FinancialWallet selectFinancialWalletById(Long id)
	{
	    return financialWalletMapper.selectFinancialWalletById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param financialWallet 信息
     * @return 集合
     */
	@Override
	public List<FinancialWalletRSP> selectFinancialWalletList(FinancialWallet financialWallet)
	{
	    return financialWalletMapper.selectFinancialWalletList(financialWallet);
	}
	
    /**
     * 新增
     * 
     * @param financialWallet 信息
     * @return 结果
     */
	@Override
	public int insertFinancialWallet(FinancialWallet financialWallet)
	{
	    return financialWalletMapper.insertFinancialWallet(financialWallet);
	}
	
	/**
     * 修改
     * 
     * @param financialWallet 信息
     * @return 结果
     */
	@Override
	public int updateFinancialWallet(FinancialWallet financialWallet)
	{
	    return financialWalletMapper.updateFinancialWallet(financialWallet);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteFinancialWalletByIds(String ids)
	{
		return financialWalletMapper.deleteFinancialWalletByIds(Convert.toStrArray(ids));
	}


	/**
	 * 根据渠道id 查询财务钱包集合
	 *
	 * @param channelIdList 渠道id集合
	 * @return 财务钱包集合
	 */
	@Override
	public List<FinancialWalletRSP> selectFinancialWalletByChannelIdList(List<Long> channelIdList) {
		return financialWalletMapper.selectFinancialWalletByChannelIdList(channelIdList);
	}

	/**
	 * 更新余额
	 * @param channelId
	 * @param finFee
	 * @return
	 */
	@Override
	public int updateFinance(Long channelId, BigDecimal finFee) {
		return financialWalletMapper.updateFinance(channelId, finFee);
	}
}
