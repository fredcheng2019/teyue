package com.ruoyi.merchant.service;
import com.ruoyi.merchant.domain.MerchantBank;
import com.ruoyi.merchant.domain.MerchantBankRSP;

import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface IMerchantBankService 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MerchantBank selectMerchantBankById(Long id);
	
	/**
     * 查询列表
     * 
     * @param merchantBank 信息
     * @return 集合
     */
	public List<MerchantBankRSP> selectMerchantBankList(MerchantBank merchantBank);
	
	/**
     * 新增
     * 
     * @param merchantBank 信息
     * @return 结果
     */
	public int insertMerchantBank(MerchantBank merchantBank);
	
	/**
     * 修改
     * 
     * @param merchantBank 信息
     * @return 结果
     */
	public int updateMerchantBank(MerchantBank merchantBank);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMerchantBankByIds(String ids);




    List<MerchantBank> selectMerchantBankListByMerchantId(Integer mpMerchantId);

}
