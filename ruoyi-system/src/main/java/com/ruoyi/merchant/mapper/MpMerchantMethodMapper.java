package com.ruoyi.merchant.mapper;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.MpMerchantMethod;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-07
 */
public interface MpMerchantMethodMapper 
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
	public int insertMpMerchantMethod(MpMerchantMethod mpMerchantMethod);
	
	/**
     * 修改
     * 
     * @param mpMerchantMethod 信息
     * @return 结果
     */
	public int updateMpMerchantMethod(MpMerchantMethod mpMerchantMethod);
	
	/**
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteMpMerchantMethodById(Long id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpMerchantMethodByIds(String[] ids);



	/**
	 * 校验商户-支付方法，不能出现相同优先级的商户-支付方法
	 * @param level		 优先级
	 * @return 			 结果
	 */
	MpMerchantMethod checkMerchantLevelUnique(@Param("level") Integer level);


	/**
	 * 根据渠道支付方法id和商户id
	 * @param channelMethodId
	 * @param merchantId
	 * @return
	 */
	List<MpMerchantMethod> selectMpMerchantMethodByChannelMthodAndMerchant(@Param("channelMethodId") Long channelMethodId, @Param("merchantId") Long merchantId);




	MpMerchantMethod checkChannelMethodIdUnique(@Param("merchantId") Long merchantId, @Param("channelMethodId")Long channelMethodId);


	/**
	 *	根据钱包id查询商户支付方法
	 * @param walletKindId	钱包id
	 * @return				商户支付方法
	 */
	List<MpMerchantMethod> selectMpMerchantMethodByWalletId(@Param("walletKindId") Long walletKindId);


	/**
	 * 根据支付类型id查询商户支付方法
	 * @param payKindId	支付类型id
	 * @return			商户支付方法集合
	 */
	List<MpMerchantMethod> selectMpMerchantMethodByPayKindId(@Param("payKindId") Long payKindId);

    List<MpMerchantMethod> selectDistinctChannelList(@Param("merchantId") Long merchantId);

    void insertMerchantMethodList(@Param("merchantMethodList") List<MpMerchantMethod> merchantMethodList);

    void updateMethodByParams(@Param("channelId") int channelId, @Param("merchantId") int merchantId, @Param("channelMethodId") int channelMethodId, @Param("payRate") BigDecimal payRate,  @Param("status") int status);

    void updateMethodBySwitchParams(@Param("channelId") int channelId, @Param("merchantId") int merchantId, @Param("channelMethodId") int channelMethodId, @Param("status") int status);

	void updateMethodByPayLimit(@Param("channelId") int channelId, @Param("merchantId") int merchantId, @Param("channelMethodId") int channelMethodId, @Param("payLimitLeft") BigDecimal payLimitLeft,  @Param("payLimitRight") BigDecimal payLimitRight);

}