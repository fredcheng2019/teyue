package com.ruoyi.basedata.service.impl;

import com.ruoyi.basedata.domain.WalletKind;
import com.ruoyi.basedata.mapper.WalletKindMapper;
import com.ruoyi.basedata.service.IWalletKindService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.merchant.domain.MpMerchantMethod;
import com.ruoyi.merchant.mapper.MpMerchantMethodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 钱包种类 服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
@Service
public class WalletKindServiceImpl implements IWalletKindService
{
	Logger logger = LoggerFactory.getLogger(WalletKindServiceImpl.class);
	/** 银行名称是否唯一的返回结果 */
	public final static String WALLETKIND_NAME_UNIQUE = "0";
	public final static String WALLETKIND_NAME_NOT_UNIQUE = "1";

	/** 钱包类型添加返回结果 */
	public final static int WALLETKIND_ADD_SUCCESS = 1;
	public final static int WALLETKIND_ADD_FALSE = -1;

	@Autowired
	private WalletKindMapper walletKindMapper;
	@Autowired
	private MpMerchantMethodMapper mpMerchantMethodMapper;


	/**
     * 查询钱包种类信息
     * 
     * @param id 钱包种类ID
     * @return 钱包种类信息
     */
    @Override
	public WalletKind selectWalletKindById(Integer id)
	{
	    return walletKindMapper.selectWalletKindById(id);
	}
	
	/**
     * 查询钱包种类列表
     * 
     * @param walletKind 钱包种类信息
     * @return 钱包种类集合
     */
	@Override
	public List<WalletKind> selectWalletKindList(WalletKind walletKind)
	{
	    return walletKindMapper.selectWalletKindList(walletKind);
	}
	
    /**
     * 新增钱包种类
     * 
     * @param walletKind 钱包种类信息
     * @return 结果
     */
	@Override
	public int insertWalletKind(WalletKind walletKind)
	{
		return walletKindMapper.insertWalletKind(walletKind);
	}
	
	/**
     * 修改钱包种类
     * 
     * @param walletKind 钱包种类信息
     * @return 结果
     */
	@Override
	public int updateWalletKind(WalletKind walletKind)
	{
		// 对应修改商户支付方法的钱包信息
		List<MpMerchantMethod> mpMerchantMethodList = mpMerchantMethodMapper.selectMpMerchantMethodByWalletId(walletKind.getId());
		if (mpMerchantMethodList.size() > 0) {
			for (MpMerchantMethod mpMerchantMethod : mpMerchantMethodList) {
				mpMerchantMethod.setWalletKindName(walletKind.getName());
				mpMerchantMethod.setWalletKindCode(walletKind.getCode());
				// 修改
				mpMerchantMethodMapper.updateMpMerchantMethod(mpMerchantMethod);
			}
		}
		// 代付交易的钱包信息
	    return walletKindMapper.updateWalletKind(walletKind);
	}

	/**
     * 删除钱包种类对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteWalletKindByIds(String ids)
	{
		return walletKindMapper.deleteWalletKindByIds(Convert.toStrArray(ids));
		// 对应渠道钱包类型删除
		// channelWalletMapper.deleteChannelWalletBywalletKindIds(Convert.toStrArray(ids));
	}

	/**
	 * 修改钱包类型状态
	 *
	 * @param walletKind 钱包类型信息
	 * @return 结果
	 */
	@Override
	public int changeStatus(WalletKind walletKind) {
		return walletKindMapper.updateWalletKind(walletKind);
	}


	/**
	 * 校验钱包名字唯一
	 *
	 * @param walletKind 钱包信息
	 * @return 结果
	 */
	@Override
	public String checkWalletkindUnique(WalletKind walletKind) {
		Long walletKindId = StringUtils.isNull(walletKind.getId()) ? -1L : walletKind.getId();
		WalletKind info = walletKindMapper.checkWalletkindUnique(walletKind.getName());
		if (StringUtils.isNotNull(info) && info.getId().longValue() != walletKindId.longValue()) {
			return WALLETKIND_NAME_NOT_UNIQUE;
		}
		return WALLETKIND_NAME_UNIQUE;
	}
}
