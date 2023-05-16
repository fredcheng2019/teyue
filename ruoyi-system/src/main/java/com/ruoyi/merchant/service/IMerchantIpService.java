package com.ruoyi.merchant.service;
import com.ruoyi.merchant.domain.MerchantIp;
import com.ruoyi.merchant.domain.MerchantIpRSP;

import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface IMerchantIpService 
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
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMerchantIpByIds(String ids);


	/**
	 * 校验该商户是否添加该IP
	 * @param merchantIp  商户ip白名单
	 * @return			  结果
	 */
	String checkMerchantIpUnique(MerchantIp merchantIp);


}
