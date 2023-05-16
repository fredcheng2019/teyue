package com.ruoyi.basedata.service;


import com.ruoyi.basedata.domain.WalletKind;

import java.util.List;

/**
 * 钱包种类 服务层
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
public interface IWalletKindService 
{
	/**
     * 查询钱包种类信息
     * 
     * @param id 钱包种类ID
     * @return 钱包种类信息
     */
	public WalletKind selectWalletKindById(Integer id);
	
	/**
     * 查询钱包种类列表
     * 
     * @param walletKind 钱包种类信息
     * @return 钱包种类集合
     */
	public List<WalletKind> selectWalletKindList(WalletKind walletKind);
	
	/**
     * 新增钱包种类
     * 
     * @param walletKind 钱包种类信息
     * @return 结果
     */
	public int insertWalletKind(WalletKind walletKind);
	
	/**
     * 修改钱包种类
     * 
     * @param walletKind 钱包种类信息
     * @return 结果
     */
	public int updateWalletKind(WalletKind walletKind);
		
	/**
     * 删除钱包种类信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteWalletKindByIds(String ids);


	/**
	 * 修改钱包类型状态
	 * @param walletKind	钱包类型信息
	 * @return				结果
	 */
    public int changeStatus(WalletKind walletKind);


	/**
	 * 校验钱包名字唯一
	 * @param walletKind	钱包信息
	 * @return				结果
	 */
	String checkWalletkindUnique(WalletKind walletKind);
}
