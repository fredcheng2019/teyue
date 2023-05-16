package com.ruoyi.merchantAccount.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.merchant.domain.MerchantBank;
import com.ruoyi.merchant.domain.MerchantBankRSP;
import com.ruoyi.merchant.mapper.MerchantBankMapper;
import com.ruoyi.merchant.service.IMerchantBankService;
import com.ruoyi.merchantAccount.domain.MerchantAccountBank;
import com.ruoyi.merchantAccount.domain.MerchantAccountBankRSP;
import com.ruoyi.merchantAccount.mapper.MerchantAccountBankMapper;
import com.ruoyi.merchantAccount.service.IMerchantAccountBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  服务层实现
 *
 * @author ruoyi
 * @date 2019-05-11
 */
@Service
public class MerchantAccountBankServiceImpl implements IMerchantAccountBankService
{
	@Autowired
	private MerchantAccountBankMapper merchantBankMapper;

	/**
     * 查询信息
     *
     * @param id ID
     * @return 信息
     */
    @Override
	public MerchantAccountBank selectMerchantBankById(Long id)
	{
	    return merchantBankMapper.selectMerchantBankById(id);
	}

	/**
     * 查询列表
     *
     * @param merchantBank 信息
     * @return 集合
     */
	@Override
	public List<MerchantAccountBankRSP> selectMerchantBankList(MerchantAccountBank merchantBank)
	{
	    return merchantBankMapper.selectMerchantBankList(merchantBank);
	}

    /**
     * 新增
     *
     * @param merchantBank 信息
     * @return 结果
     */
	@Override
	public int insertMerchantBank(MerchantAccountBank merchantBank)
	{
	    return merchantBankMapper.insertMerchantBank(merchantBank);
	}

	/**
     * 修改
     *
     * @param merchantBank 信息
     * @return 结果
     */
	@Override
	public int updateMerchantBank(MerchantAccountBank merchantBank)
	{
	    return merchantBankMapper.updateMerchantBank(merchantBank);
	}

	/**
     * 删除对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMerchantBankByIds(String ids)
	{
		return merchantBankMapper.deleteMerchantBankByIds(Convert.toStrArray(ids));
	}


	/**
	 * 查询商户已添加的银行
	 * @param mpMerchantId	商户id
	 * @return				商户银行列表
	 */
	@Override
	public List<MerchantAccountBank> selectMerchantBankListByMerchantId(Integer mpMerchantId) {
		return merchantBankMapper.selectMerchantBankListByMerchantId(mpMerchantId);
	}


}
