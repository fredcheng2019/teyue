package com.ruoyi.financial.service;

import com.ruoyi.financial.domain.FinancialWallet;
import com.ruoyi.financial.domain.FinancialWalletRSP;

import java.math.BigDecimal;
import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
public interface IFinancialWalletService 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public FinancialWallet selectFinancialWalletById(Long id);
	
	/**
     * 查询列表
     * 
     * @param financialWallet 信息
     * @return 集合
     */
	public List<FinancialWalletRSP> selectFinancialWalletList(FinancialWallet financialWallet);
	
	/**
     * 新增
     * 
     * @param financialWallet 信息
     * @return 结果
     */
	public int insertFinancialWallet(FinancialWallet financialWallet);
	
	/**
     * 修改
     * 
     * @param financialWallet 信息
     * @return 结果
     */
	public int updateFinancialWallet(FinancialWallet financialWallet);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteFinancialWalletByIds(String ids);


	/**
	 * 根据渠道id 查询财务钱包集合
	 * @param channelIdList	渠道id集合
	 * @return				财务钱包集合
	 */
	List<FinancialWalletRSP> selectFinancialWalletByChannelIdList(List<Long> channelIdList);

	/**
	 * 更新余额
	 * @param channelId
	 * @param finFee
	 * @return
	 */
	int updateFinance(Long channelId, BigDecimal finFee);
}
