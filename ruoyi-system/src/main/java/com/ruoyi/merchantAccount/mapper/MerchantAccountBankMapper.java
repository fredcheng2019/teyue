package com.ruoyi.merchantAccount.mapper;
import com.ruoyi.merchant.domain.MerchantBank;
import com.ruoyi.merchant.domain.MerchantBankRSP;
import com.ruoyi.merchantAccount.domain.MerchantAccountBank;
import com.ruoyi.merchantAccount.domain.MerchantAccountBankRSP;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface MerchantAccountBankMapper
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
    List<MerchantAccountBank> selectMerchantBankListByMerchantId(@Param("mpMerchantId") Integer mpMerchantId);

}