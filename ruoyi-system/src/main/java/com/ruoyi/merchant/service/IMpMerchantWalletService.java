package com.ruoyi.merchant.service;

import com.ruoyi.merchant.domain.MerchantWithdrawal;
import com.ruoyi.merchant.domain.MpMerchantWallet;
import com.ruoyi.merchant.domain.MpMerchantWalletVo;
import com.ruoyi.system.domain.SysUser;

import java.math.BigDecimal;
import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public interface IMpMerchantWalletService 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MpMerchantWallet selectMpMerchantWalletById(Integer id);
	
	/**
     * 查询列表
     * 
     * @param mpMerchantWallet 信息
     * @return 集合
     */
	public List<MpMerchantWalletVo> selectMpMerchantWalletList(MpMerchantWallet mpMerchantWallet);

	/**
	 * 统计
	 * @param mpMerchantWallet 信息
	 * @return 集合
	 */
	MpMerchantWalletVo selectStatistics(MpMerchantWallet mpMerchantWallet);
	
	/**
     * 新增
     * 
     * @param mpMerchantWallet 信息
     * @return 结果
     */
	public int insertMpMerchantWallet(MpMerchantWallet mpMerchantWallet);
	
	/**
     * 修改
     * 
     * @param mpMerchantWallet 信息
     * @return 结果
     */
	public int updateMpMerchantWallet(MpMerchantWallet mpMerchantWallet);
	/**
	 * 修改
	 *
	 * @param mpMerchantWallet 信息
	 * @return 结果
	 */
	 int updateAdvanceBalance(SysUser user, MpMerchantWallet mpMerchantWallet);
	 int updateAccountBalance(SysUser user, MpMerchantWallet mpMerchantWallet);

	 int updateFrozenBalance(SysUser user, MpMerchantWallet mpMerchantWallet);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpMerchantWalletByIds(String ids);

	/**
	 * 校验商户钱包是否存在
	 * @param mpMerchantWallet
	 */
	MpMerchantWallet checkMerchantWallet(MpMerchantWallet mpMerchantWallet);


	/**
	 * 查询渠道钱包
	 * @param status  状态
	 * @return		  渠道钱包相关信息集合
	 */
    List<MerchantWithdrawal> channelWalletList(int status);


	MpMerchantWalletVo selectMpMerchantWalletVoById(Integer id);

	MpMerchantWalletVo selectMpMerchantWalletVoByMerchantId(Integer id);

	/**
	 * 更新余额
	 * @param merchantId
	 * @param walletKindId
	 * @param mchFee
	 * @return
	 */
	int updateBalance(Long merchantId, Integer walletKindId, BigDecimal mchFee);

}
