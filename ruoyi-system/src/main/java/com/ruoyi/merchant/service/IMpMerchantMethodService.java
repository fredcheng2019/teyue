package com.ruoyi.merchant.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.MpMerchantMethod;
import com.ruoyi.merchant.domain.MpMerchantMethodRSP;

import java.math.BigDecimal;
import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-07
 */
public interface IMpMerchantMethodService 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MpMerchantMethod selectMpMerchantMethodById(Long id);
	
	/**
     * 查询列表
     * 
     * @param mpMerchantMethod 信息
     * @return 集合
     */
	public List<MpMerchantMethod> selectMpMerchantMethodList(MpMerchantMethod mpMerchantMethod);
	
	/**
     * 新增
     * 
     * @param mpMerchantMethod 信息
     * @return 结果
     */
	public AjaxResult insertMpMerchantMethod(MpMerchantMethod mpMerchantMethod);
	
	/**
     * 修改
     * 
     * @param mpMerchantMethod 信息
     * @return 结果
     */
	public AjaxResult updateMpMerchantMethod(MpMerchantMethod mpMerchantMethod);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpMerchantMethodByIds(String ids);

    int changeStatus(MpMerchantMethod mpMerchantMethod);


	/**
	 * 校验商户-支付方法，不能出现相同优先级的商户-支付方法
	 * @param mpMerchantMethod   商户-支付方法
	 * @return				     结果
	 */
	String checkMerchantLevelUnique(MpMerchantMethod mpMerchantMethod);


	/**
	 * 根据渠道支付方法id和商户id
	 * @param channelMethodId
	 * @param merchantId
	 * @return
	 */
	List<MpMerchantMethod> selectMpMerchantMethodByChannelMthodAndMerchant(Long channelMethodId, Long merchantId);


	/**
	 * 校验商户-支付方法，同一商户不能出现相同-渠道支付方法
	 * @param mpMerchantMethod	商户-支付方法
	 * @return					结果
	 */
	String checkChannelMethodIdUnique(MpMerchantMethod mpMerchantMethod);


	/**
	 * 查询商户已添加的渠道（去重查询）
	 */
    List<MpMerchantMethod> selectDistinctChannelList(Long merchantId);


	/**
	 * 商户支付方法-设置费率
	 */
	AjaxResult updateMethodByParams(int channelId, int merchantId, int channelMethodId, BigDecimal payRate, int status);


	/**
	 * 商户支付方法-批量开关
	 */
    AjaxResult updateMethodBySwitchParams(int channelId, int merchantId, int channelMethodId, int status);

	/**
	 * 商户支付方法-设置批量支付限额
	 */
	AjaxResult updateMethodByPayLimit(int channelId, int merchantId, int channelMethodId, BigDecimal payLimitLeft, BigDecimal payLimitRight);

}
