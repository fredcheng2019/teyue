package com.ruoyi.merchant.mapper;

import com.ruoyi.merchant.domain.MerchantWithdrawal;
import com.ruoyi.merchant.domain.MpMerchantWallet;
import com.ruoyi.merchant.domain.MpMerchantWalletVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *  数据层
 *
 * @author ruoyi
 * @date 2019-05-06
 */
public interface MpMerchantWalletMapper
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
	public MpMerchantWalletVo selectStatistics(MpMerchantWallet mpMerchantWallet);

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
     * 删除
     *
     * @param id ID
     * @return 结果
     */
	public int deleteMpMerchantWalletById(Integer id);

	/**
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpMerchantWalletByIds(String[] ids);

	/**
	 * 检测商户钱包是否唯一
	 * @param mpMerchantWallet
	 * @return
	 */
    MpMerchantWallet checkMerchantWallet(MpMerchantWallet mpMerchantWallet);


	/**
	 * 根据商户id和钱包类型id  查询商户钱包
	 * @param merchantId	商户id
	 * @param walletKindId	钱包类型id
	 * @return				商户钱包集合
	 */
	List<MpMerchantWallet> selectMpMerchantWalletByMerchantIdAndWalletKindId(@Param("merchantId") Long merchantId, @Param("walletKindId") Long walletKindId);


	/**
	 * 查询渠道钱包
	 *
	 * @param status 状态
	 * @return 渠道钱包相关信息集合
	 */
    List<MerchantWithdrawal> channelWalletList(@Param("status") int status);



	/**
	 * 根据商户id和钱包id获取商户钱包，同时要钱够
	 * @param merchantId
	 * @param walletKindId
	 * @param total_fee
	 * @return
	 */
    MpMerchantWallet getWalletByMerchantIdAndWalletKindIdAndEnoughMoney(@Param("merchantId") Long merchantId, @Param("walletKindId") Long walletKindId, @Param("total_fee") BigDecimal total_fee);


	MpMerchantWalletVo selectMpMerchantWalletVoById(@Param("id") Integer id);

	MpMerchantWalletVo selectMpMerchantWalletVoByMerchantId(@Param("id") Integer id);

	/**
	 * 更新余额
	 * @param merchantId
	 * @param walletKindId
	 * @param mchFee
	 * @return
	 */
	int updateBalance(@Param("merchantId")Long merchantId, @Param("walletKindId")Integer walletKindId, @Param("mchFee")BigDecimal mchFee);

	/**
	 * 查询信息
	 *
	 * @param id ID
	 * @return 信息
	 */
	public MpMerchantWallet selectMpMerchantWalletByIdForUpdate(Integer id);
}
