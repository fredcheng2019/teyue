package com.ruoyi.merchant.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.mapper.MpMerchantMapper;
import com.ruoyi.merchant.mapper.MpMerchantWalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.merchant.mapper.MpMerchantMethodMapper;
import com.ruoyi.merchant.domain.MpMerchantMethod;
import com.ruoyi.merchant.service.IMpMerchantMethodService;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-07
 */
@Service
public class MpMerchantMethodServiceImpl implements IMpMerchantMethodService 
{

	/** 商户支付方法添加时检验是否唯一的返回结果 */
	public final static String METHOD_UNIQUE = "0";
	public final static String METHOD_NOT_UNIQUE = "1";


	@Autowired
	private MpMerchantMethodMapper mpMerchantMethodMapper;
	@Autowired
	private MpMerchantWalletMapper mpMerchantWalletMapper;
	@Autowired
	private MpMerchantMapper mpMerchantMapper;






	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public MpMerchantMethod selectMpMerchantMethodById(Long id)
	{
	    return mpMerchantMethodMapper.selectMpMerchantMethodById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param mpMerchantMethod 信息
     * @return 集合
     */
	@Override
	public List<MpMerchantMethod> selectMpMerchantMethodList(MpMerchantMethod mpMerchantMethod)
	{
	    return mpMerchantMethodMapper.selectMpMerchantMethodList(mpMerchantMethod);
	}
	
    /**
     * 新增
     * 
     * @param mpMerchantMethod 信息
     * @return 结果
     */
	@Override
	public AjaxResult insertMpMerchantMethod(MpMerchantMethod mpMerchantMethod)
	{


		return AjaxResult.success("添加成功！");
	}
	
	/**
     * 修改
     * 
     * @param mpMerchantMethod 信息
     * @return 结果
     */
	@Override
	public AjaxResult updateMpMerchantMethod(MpMerchantMethod mpMerchantMethod)
	{
		return AjaxResult.success("修改成功！");
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMpMerchantMethodByIds(String ids)
	{
		return mpMerchantMethodMapper.deleteMpMerchantMethodByIds(Convert.toStrArray(ids));
	}

	@Override
	public int changeStatus(MpMerchantMethod mpMerchantMethod) {
		return mpMerchantMethodMapper.updateMpMerchantMethod(mpMerchantMethod);
	}


	/**
	 * 校验商户-支付方法，不能出现相同优先级的商户-支付方法
	 * @param mpMerchantMethod 商户-支付方法
	 * @return 结果
	 */
	@Override
	public String checkMerchantLevelUnique(MpMerchantMethod mpMerchantMethod) {
		Long mpMerchantMethodId = StringUtils.isNull(mpMerchantMethod.getId()) ? -1L : mpMerchantMethod.getId();
		MpMerchantMethod info = mpMerchantMethodMapper.checkMerchantLevelUnique(mpMerchantMethod.getLevel());
		if (StringUtils.isNotNull(info) && info.getId().longValue() != mpMerchantMethodId){
			return METHOD_NOT_UNIQUE;
		}
		return METHOD_UNIQUE;
	}


	/**
	 * 根据渠道支付方法id和商户id
	 *
	 * @param channelMethodId
	 * @param merchantId
	 * @return
	 */
	@Override
	public List<MpMerchantMethod> selectMpMerchantMethodByChannelMthodAndMerchant(Long channelMethodId, Long merchantId) {
		return mpMerchantMethodMapper.selectMpMerchantMethodByChannelMthodAndMerchant(channelMethodId, merchantId);
	}



	/**
	 * 校验商户-支付方法，同一商户不能出现相同-渠道支付方法
	 *
	 * @param mpMerchantMethod 商户-支付方法
	 * @return 结果
	 */
	@Override
	public String checkChannelMethodIdUnique(MpMerchantMethod mpMerchantMethod) {
		Long mpMerchantMethodId = StringUtils.isNull(mpMerchantMethod.getId()) ? -1L : mpMerchantMethod.getId();
		MpMerchantMethod info = mpMerchantMethodMapper.checkChannelMethodIdUnique(mpMerchantMethod.getMerchantId(), mpMerchantMethod.getChannelMethodId());
		if (StringUtils.isNotNull(info) && info.getId().longValue() != mpMerchantMethodId){
			return METHOD_NOT_UNIQUE;
		}
		return METHOD_UNIQUE;
	}


	/**
	 * 查询商户已添加的渠道（去重查询）
	 *
	 * @param merchantId
	 */
	@Override
	public List<MpMerchantMethod> selectDistinctChannelList(Long merchantId) {
		return mpMerchantMethodMapper.selectDistinctChannelList(merchantId);
	}


	/**
	 * 商户支付方法-设置费率
	 */
	@Override
	public AjaxResult updateMethodByParams(int channelId, int merchantId, int channelMethodId, BigDecimal payRate, int status) {
		try {
			mpMerchantMethodMapper.updateMethodByParams(channelId, merchantId, channelMethodId, payRate, status);
			return AjaxResult.success("操作成功！");
		}catch (Exception e){
			e.printStackTrace();
			return AjaxResult.error("操作失败！");
		}
	}


	/**
	 * 商户支付方法-批量开关
	 *
	 * @param channelId
	 * @param merchantId
	 * @param channelMethodId
	 * @param status
	 */
	@Override
	public AjaxResult updateMethodBySwitchParams(int channelId, int merchantId, int channelMethodId, int status) {
		try {
			mpMerchantMethodMapper.updateMethodBySwitchParams(channelId, merchantId, channelMethodId, status);
			return AjaxResult.success("操作成功！");
		}catch (Exception e){
			e.printStackTrace();
			return AjaxResult.error("操作失败！");
		}
	}

	@Override
	public AjaxResult updateMethodByPayLimit(int channelId, int merchantId, int channelMethodId, BigDecimal payLimitLeft, BigDecimal payLimitRight) {
		// 支付限额左不能大于或者等于支付限额右
		int PayFlag = payLimitLeft.compareTo(payLimitRight);
		if (PayFlag == 1) {
			return AjaxResult.error("操作失败！商户支付方法-支付限额范围不正确！<br/> 支付限额左 > 支付限额右");
		} else if (PayFlag == 0) {
			return AjaxResult.error("操作失败！商户支付方法-支付限额范围不正确！<br/> 支付限额左 = 支付限额右");
		}
		if (merchantId == -1) {
			return AjaxResult.error("操作失败！请选择商户");
		}
		try {
			mpMerchantMethodMapper.updateMethodByPayLimit(channelId, merchantId, channelMethodId, payLimitLeft, payLimitRight);
			return AjaxResult.success("操作成功！");
		}catch (Exception e){
			e.printStackTrace();
			return AjaxResult.error("操作失败！");
		}
	}
}
