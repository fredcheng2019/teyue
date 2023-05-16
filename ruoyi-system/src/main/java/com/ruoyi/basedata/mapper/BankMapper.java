package com.ruoyi.basedata.mapper;

import com.ruoyi.basedata.domain.Bank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 银行种类 数据层
 * 
 * @author ruoyi
 * @date 2019-05-05
 */
public interface BankMapper 
{
	/**
     * 查询银行种类信息
     * 
     * @param id 银行种类ID
     * @return 银行种类信息
     */
	public Bank selectBankById(Integer id);
	
	/**
     * 查询银行种类列表
     * 
     * @param bank 银行种类信息
     * @return 银行种类集合
     */
	public List<Bank> selectBankList(Bank bank);
	
	/**
     * 新增银行种类
     * 
     * @param bank 银行种类信息
     * @return 结果
     */
	public int insertBank(Bank bank);
	
	/**
     * 修改银行种类
     * 
     * @param bank 银行种类信息
     * @return 结果
     */
	public int updateBank(Bank bank);
	
	/**
     * 删除银行种类
     * 
     * @param id 银行种类ID
     * @return 结果
     */
	public int deleteBankById(Integer id);
	
	/**
     * 批量删除银行种类
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBankByIds(String[] ids);


	/**
	 * +
	 * 校验名字是否唯一
	 *
	 * @param name 银行信息
	 * @return 结果
	 */
    Bank checkBankNameUnique(String name);


	/**
	 * 查询不在bankIds里的银行
	 *
	 * @param bankIds 银行id集合
	 * @return 银行集合
	 */
	List<Bank> selectBankListWithoutBankIds(@Param("bankIds") List<Integer> bankIds);

}