package com.ruoyi.basedata.service.impl;

import java.util.List;

import com.ruoyi.basedata.domain.PayKind;
import com.ruoyi.basedata.mapper.PayKindMapper;
import com.ruoyi.basedata.service.IPayKindService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.merchant.domain.MpMerchantMethod;
import com.ruoyi.merchant.mapper.MpMerchantMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
@Service
public class PayKindServiceImpl implements IPayKindService
{
	private static final String PAYKIND_NAME_NOT_UNIQUE = "1";
	private static final String PAYKIND_NAME_UNIQUE = "0";
	@Autowired
	private PayKindMapper payKindMapper;
	@Autowired
	private MpMerchantMethodMapper merchantMethodMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public PayKind selectPayKindById(Long id)
	{
	    return payKindMapper.selectPayKindById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param payKind 信息
     * @return 集合
     */
	@Override
	public List<PayKind> selectPayKindList(PayKind payKind)
	{
	    return payKindMapper.selectPayKindList(payKind);
	}
	
    /**
     * 新增
     * 
     * @param payKind 信息
     * @return 结果
     */
	@Override
	public int insertPayKind(PayKind payKind)
	{
	    return payKindMapper.insertPayKind(payKind);
	}
	
	/**
     * 修改
     * 
     * @param payKind 信息
     * @return 结果
     */
	@Override
	public int updatePayKind(PayKind payKind)
	{
		// 对应修改商户支付方法的支付类型信息
		List<MpMerchantMethod> merchantMethodList = merchantMethodMapper.selectMpMerchantMethodByPayKindId(payKind.getId());
		if (merchantMethodList.size() > 0) {
			for (MpMerchantMethod merchantMethod : merchantMethodList) {
				merchantMethod.setPayKindCode(payKind.getCode());
				merchantMethod.setPayKindName(payKind.getName());
				// 更新数据
				merchantMethodMapper.updateMpMerchantMethod(merchantMethod);
			}
		}

	    return payKindMapper.updatePayKind(payKind);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePayKindByIds(String ids)
	{
		return payKindMapper.deletePayKindByIds(Convert.toStrArray(ids));
	}

	/**
	 * 修改支付类型状态
	 *
	 * @param payKind 支付类型信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(PayKind payKind) {
		return payKindMapper.updatePayKind(payKind);
	}

	/**
	 * 校验字唯一
	 *
	 * @param payKind 钱包信息
	 * @return 结果
	 */
	@Override
	public String checkPaykindUnique(PayKind payKind) {
		Long payKindId = StringUtils.isNull(payKind.getId()) ? -1L : payKind.getId();
		PayKind info = payKindMapper.checkPaykindUnique(payKind.getName());
		if (StringUtils.isNotNull(info) && info.getId().longValue() != payKindId.longValue()) {
			return PAYKIND_NAME_NOT_UNIQUE;
		}
		return PAYKIND_NAME_UNIQUE;
	}
}
