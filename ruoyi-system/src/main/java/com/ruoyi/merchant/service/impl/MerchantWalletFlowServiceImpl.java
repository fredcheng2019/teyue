package com.ruoyi.merchant.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.merchant.domain.MerchantWalletFlow;
import com.ruoyi.merchant.service.IMerchantWalletFlowService;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2020-08-31
 */
@Service
public class MerchantWalletFlowServiceImpl implements IMerchantWalletFlowService 
{
	@Autowired
	private MerchantWalletFlowMapper merchantWalletFlowMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public MerchantWalletFlow selectMerchantWalletFlowById(Long id)
	{
	    return merchantWalletFlowMapper.selectMerchantWalletFlowById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param merchantWalletFlow 信息
     * @return 集合
     */
	@Override
	public List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow merchantWalletFlow)
	{
	    return merchantWalletFlowMapper.selectMerchantWalletFlowList(merchantWalletFlow);
	}
	
    /**
     * 新增
     * 
     * @param merchantWalletFlow 信息
     * @return 结果
     */
	@Override
	public int insertMerchantWalletFlow(MerchantWalletFlow merchantWalletFlow)
	{
	    return merchantWalletFlowMapper.insertMerchantWalletFlow(merchantWalletFlow);
	}
	
	/**
     * 修改
     * 
     * @param merchantWalletFlow 信息
     * @return 结果
     */
	@Override
	public int updateMerchantWalletFlow(MerchantWalletFlow merchantWalletFlow)
	{
	    return merchantWalletFlowMapper.updateMerchantWalletFlow(merchantWalletFlow);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMerchantWalletFlowByIds(String ids)
	{
		return merchantWalletFlowMapper.deleteMerchantWalletFlowByIds(Convert.toStrArray(ids));
	}
	
}
