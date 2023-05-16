package com.ruoyi.merchantAccount.service;
import com.ruoyi.merchant.domain.MerchantBank;
import com.ruoyi.merchant.domain.MerchantBankRSP;
import com.ruoyi.merchantAccount.domain.MerchantAccountBank;
import com.ruoyi.merchantAccount.domain.MerchantAccountBankRSP;

import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface IMerchantAccountBankService
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MerchantAccountBank selectMerchantBankById(Long id);
	
	/**
     * 查询列表
     * 
     * @param merchantBank 信息
     * @return 集合
     */
	public List<MerchantAccountBankRSP> selectMerchantBankList(MerchantAccountBank merchantBank);
	
	/**
     * 新增
     * 
     * @param merchantBank 信息
     * @return 结果
     */
	public int insertMerchantBank(MerchantAccountBank merchantBank);
	
	/**
     * 修改
     * 
     * @param merchantBank 信息
     * @return 结果
     */
	public int updateMerchantBank(MerchantAccountBank merchantBank);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMerchantBankByIds(String ids);




    List<MerchantAccountBank> selectMerchantBankListByMerchantId(Integer mpMerchantId);

}
