package com.ruoyi.financial.mapper;

import com.ruoyi.financial.domain.MpFinancialSetting;
import java.util.List;	

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-13
 */
public interface MpFinancialSettingMapper 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MpFinancialSetting selectMpFinancialSettingById(Long id);
	
	/**
     * 查询列表
     * 
     * @param mpFinancialSetting 信息
     * @return 集合
     */
	public List<MpFinancialSetting> selectMpFinancialSettingList(MpFinancialSetting mpFinancialSetting);
	
	/**
     * 新增
     * 
     * @param mpFinancialSetting 信息
     * @return 结果
     */
	public int insertMpFinancialSetting(MpFinancialSetting mpFinancialSetting);
	
	/**
     * 修改
     * 
     * @param mpFinancialSetting 信息
     * @return 结果
     */
	public int updateMpFinancialSetting(MpFinancialSetting mpFinancialSetting);
	
	/**
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deleteMpFinancialSettingById(Long id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpFinancialSettingByIds(String[] ids);
	
}