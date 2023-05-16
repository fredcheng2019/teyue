package com.ruoyi.merchant.mapper;

import com.ruoyi.merchant.domain.MerchantIp;
import com.ruoyi.merchant.domain.MerchantIpRSP;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface MerchantIpMapper 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MerchantIp selectMerchantIpById(Long id);
	
	/**
     * 查询列表
     * 
     * @param merchantIp 信息
     * @return 集合
     */
	public List<MerchantIpRSP> selectMerchantIpList(MerchantIp merchantIp);
	
	/**
     * 新增
     * 
     * @param merchantIp 信息
     * @return 结果
     */
	public int insertMerchantIp(MerchantIp merchantIp);
	
	/**
     * 修改
     * 
     * @param merchantIp 信息
     * @return 结果
     */
	public int updateMerchantIp(MerchantIp merchantIp);
	
	/**
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteMerchantIpById(Long id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMerchantIpByIds(String[] ids);



	/**
	 * 校验该商户是否添加该IP
	 *
	 * @param merchantId 商户ip白名单
	 * @param ip 		 ip
	 * @return 结果
	 */
	MerchantIp checkMerchantIpUnique(@Param("merchantId") Long merchantId, @Param("ip") String ip);

}