package com.ruoyi.merchantAccount.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.merchant.domain.MpMerchantUpdateReq;
import com.ruoyi.merchantAccount.domain.MerchantSetting;
import com.ruoyi.merchantAccount.mapper.MerchantSettingMapper;
import com.ruoyi.merchantAccount.service.IMerchantSettingService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
@Service
public class MerchantSettingServiceImpl implements IMerchantSettingService
{
	Logger logger = LoggerFactory.getLogger(MerchantSettingServiceImpl.class);

	@Autowired
	private MerchantSettingMapper merchantSettingMapper;

	@Autowired
	private SysUserMapper userMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public MerchantSetting selectMpMerchantById(Long id)
	{
	    return merchantSettingMapper.selectMpMerchantById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param merchantSetting 信息
     * @return 集合
     */
	@Override
	public List<MerchantSetting> selectMpMerchantList(MerchantSetting merchantSetting)
	{
	    return merchantSettingMapper.selectMpMerchantList(merchantSetting);
	}

	/**
     * 修改
     * 
     * @param merchantSetting 信息
     * @return 结果
     */
	@Override
	public int updateMpMerchant(MpMerchantUpdateReq merchantSetting)
	{
		// 同时更新商户支付方法的商户信息
		merchantSettingMapper.updateMerchantMethod(merchantSetting);
		if(StringUtils.isNotEmpty(merchantSetting.getGoogleSecret())){
			SysUser user = new SysUser();
			user.setUserName(merchantSetting.getUserName());
			user.setEmail(merchantSetting.getEmail());
			user.setPhonenumber(merchantSetting.getPhonenumber());
			user.setSex(merchantSetting.getSex());

			user.setGoogleSecret(merchantSetting.getGoogleSecret());
			user.setUserId(merchantSetting.getUserId());
			userMapper.updateUser(user);
		}
		return merchantSettingMapper.updateMpMerchant(merchantSetting);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMpMerchantByIds(String ids)
	{
		return merchantSettingMapper.deleteMpMerchantByIds(Convert.toStrArray(ids));
	}

}
