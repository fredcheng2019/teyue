package com.ruoyi.merchantAccount.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import com.ruoyi.merchant.domain.MpMerchantUpdateReq;
import com.ruoyi.merchantAccount.domain.MerchantSetting;

import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public interface IMerchantSettingService
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
//	public AjaxResult insertMpMerchant(MerchantSetting merchantSetting);
	
	/**
     * 修改
     * 
     * @param merchantSetting 信息
     * @return 结果
     */
	public int updateMpMerchant(MpMerchantUpdateReq merchantSetting);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpMerchantByIds(String ids);

}
