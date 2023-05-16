package com.ruoyi.merchant.service.impl;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.merchant.domain.MerchantIp;
import com.ruoyi.merchant.domain.MerchantIpRSP;
import com.ruoyi.merchant.mapper.MerchantIpMapper;
import com.ruoyi.merchant.service.IMerchantIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
@Service
public class MerchantIpServiceImpl implements IMerchantIpService
{
	@Autowired
	private MerchantIpMapper merchantIpMapper;
	/** 商户ip是否唯一的返回结果 */
	public final static String MERCHANT_IP_UNIQUE = "0";
	public final static String MERCHANT_IP_NOT_UNIQUE = "1";

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public MerchantIp selectMerchantIpById(Long id)
	{
	    return merchantIpMapper.selectMerchantIpById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param merchantIp 信息
     * @return 集合
     */
	@Override
	public List<MerchantIpRSP> selectMerchantIpList(MerchantIp merchantIp)
	{
	    return merchantIpMapper.selectMerchantIpList(merchantIp);
	}
	
    /**
     * 新增
     * 
     * @param merchantIp 信息
     * @return 结果
     */
	@Override
	public int insertMerchantIp(MerchantIp merchantIp)
	{
	    return merchantIpMapper.insertMerchantIp(merchantIp);
	}
	
	/**
     * 修改
     * 
     * @param merchantIp 信息
     * @return 结果
     */
	@Override
	public int updateMerchantIp(MerchantIp merchantIp)
	{
	    return merchantIpMapper.updateMerchantIp(merchantIp);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMerchantIpByIds(String ids)
	{
		return merchantIpMapper.deleteMerchantIpByIds(Convert.toStrArray(ids));
	}


	/**
	 * 校验该商户是否添加该IP
	 *
	 * @param merchantIp 商户ip白名单
	 * @return 结果
	 */
	@Override
	public String checkMerchantIpUnique(MerchantIp merchantIp) {
		Long merchantIpId = StringUtils.isNull(merchantIp.getId()) ? -1L : merchantIp.getId();
		MerchantIp info = merchantIpMapper.checkMerchantIpUnique(merchantIp.getMerchantId(), merchantIp.getIp());
		if (StringUtils.isNotNull(info) && info.getId().longValue() != merchantIpId) {
			return MERCHANT_IP_NOT_UNIQUE;
		}
		return MERCHANT_IP_UNIQUE;
	}


}
