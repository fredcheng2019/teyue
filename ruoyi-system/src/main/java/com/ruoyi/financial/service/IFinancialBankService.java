package com.ruoyi.financial.service;

import com.ruoyi.financial.domain.FinancialBank;
import com.ruoyi.financial.domain.FinancialBankRSP;

import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-08
 */
public interface IFinancialBankService 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public FinancialBank selectFinancialBankById(Long id);
	
	/**
     * 查询列表
     * 
     * @param financialBank 信息
     * @return 集合
     */
	public List<FinancialBankRSP> selectFinancialBankList(FinancialBank financialBank);
	
	/**
     * 新增
     * 
     * @param financialBank 信息
     * @return 结果
     */
	public int insertFinancialBank(FinancialBank financialBank);
	
	/**
     * 修改
     * 
     * @param financialBank 信息
     * @return 结果
     */
	public int updateFinancialBank(FinancialBank financialBank);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteFinancialBankByIds(String ids);
	
}
