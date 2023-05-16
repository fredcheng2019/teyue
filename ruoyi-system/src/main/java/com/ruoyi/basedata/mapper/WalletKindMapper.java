package com.ruoyi.basedata.mapper;


import com.ruoyi.basedata.domain.WalletKind;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 钱包种类 数据层
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
public interface WalletKindMapper 
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
     * 删除钱包种类
     * 
     * @param id 钱包种类ID
     * @return 结果
     */
	public int deleteWalletKindById(Integer id);
	
	/**
     * 批量删除钱包种类
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteWalletKindByIds(String[] ids);

	/**
	 * 校验钱包名称唯一性
	 * @param name	 钱包名称
	 * @return		 结果
	 */
    WalletKind checkWalletkindUnique(String name);


	/**
	 * 查询刚新增的钱包类型，拿到id
	 * @param name	钱包名称
	 * @return		钱包类型
	 */
	WalletKind selectWalletKindByName(@Param("name") String name);

}