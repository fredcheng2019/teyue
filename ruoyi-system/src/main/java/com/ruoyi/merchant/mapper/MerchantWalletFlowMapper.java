package com.ruoyi.merchant.mapper;

import com.ruoyi.merchant.domain.MerchantWalletFlow;
import java.util.List;	

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
public interface MerchantWalletFlowMapper 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MerchantWalletFlow selectMerchantWalletFlowById(Long id);
	
	/**
     * 查询列表
     * 
     * @param merchantWalletFlow 信息
     * @return 集合
     */
	public List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow merchantWalletFlow);
	
	/**
     * 新增
     * 
     * @param merchantWalletFlow 信息
     * @return 结果
     */
	public int insertMerchantWalletFlow(MerchantWalletFlow merchantWalletFlow);
	
	/**
     * 修改
     * 
     * @param merchantWalletFlow 信息
     * @return 结果
     */
	public int updateMerchantWalletFlow(MerchantWalletFlow merchantWalletFlow);
	
	/**
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteMerchantWalletFlowById(Long id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMerchantWalletFlowByIds(String[] ids);
	
}