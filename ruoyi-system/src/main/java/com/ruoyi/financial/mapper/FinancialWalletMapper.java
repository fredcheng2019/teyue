package com.ruoyi.financial.mapper;

import com.ruoyi.financial.domain.FinancialWallet;
import com.ruoyi.financial.domain.FinancialWalletRSP;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
public interface FinancialWalletMapper 
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
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteFinancialWalletById(Long id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteFinancialWalletByIds(String[] ids);


	/**
	 * 根据渠道id和钱包类型id查询
	 * @param channelId		渠道id
	 * @param walletKindId	钱包类型id
	 * @return	财务钱包
	 */
    FinancialWallet selectFinancialWalletByChannelIdAndWalletKindId(@Param("channelId") Long channelId, @Param("walletKindId") Long walletKindId);


	/**
	 * 根据渠道id查询财务钱包
	 * @param channelId	渠道id
	 * @return			财务钱包
	 */
	FinancialWallet selectFinancialWalletByChannelId(@Param("channelId") Long channelId);



	/**
	 * 根据渠道id 查询财务钱包集合
	 *
	 * @param channelIdList 渠道id集合
	 * @return 财务钱包集合
	 */
	List<FinancialWalletRSP> selectFinancialWalletByChannelIdList(@Param("channelIdList") List<Long> channelIdList);

	/**
	 * 更新余额
	 * @param channelId
	 * @param finFee
	 * @return
	 */
	int updateFinance(@Param("channelId")Long channelId, @Param("finFee")BigDecimal finFee);

}