package com.ruoyi.merchant.mapper;
import com.ruoyi.merchant.domain.MerchantBank;
import com.ruoyi.merchant.domain.MerchantBankRSP;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface MerchantBankMapper 
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
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteMerchantBankById(Long id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMerchantBankByIds(String[] ids);



	/**
	 * 查询商户已添加的银行
	 * @param mpMerchantId	商户id
	 * @return				商户银行列表
	 */
    List<MerchantBank> selectMerchantBankListByMerchantId(@Param("mpMerchantId") Integer mpMerchantId);

}