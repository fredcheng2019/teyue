package com.ruoyi.merchantAccount.mapper;

import com.ruoyi.merchantAccount.domain.MerchantSetting;

import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public interface MerchantSettingMapper
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MerchantSetting selectMpMerchantById(Long id);
	
	/**
     * 查询列表
     * 
     * @param merchantSetting 信息
     * @return 集合
     */
	public List<MerchantSetting> selectMpMerchantList(MerchantSetting merchantSetting);
	
	/**
     * 新增
     * 
     * @param merchantSetting 信息
     * @return 结果
     */
	public int insertMpMerchant(MerchantSetting merchantSetting);
	
	/**
     * 修改
     * 
     * @param merchantSetting 信息
     * @return 结果
     */
	public int updateMpMerchant(MerchantSetting merchantSetting);
	
	/**
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteMpMerchantById(Integer id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpMerchantByIds(String[] ids);

	int updateMerchantMethod(MerchantSetting merchantSetting);

}