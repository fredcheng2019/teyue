package com.ruoyi.financial.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.financial.mapper.MpFinancialSettingMapper;
import com.ruoyi.financial.domain.MpFinancialSetting;
import com.ruoyi.financial.service.IMpFinancialSettingService;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-13
 */
@Service
public class MpFinancialSettingServiceImpl implements IMpFinancialSettingService 
{
	@Autowired
	private MpFinancialSettingMapper mpFinancialSettingMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public MpFinancialSetting selectMpFinancialSettingById(Long id)
	{
	    return mpFinancialSettingMapper.selectMpFinancialSettingById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param mpFinancialSetting 信息
     * @return 集合
     */
	@Override
	public List<MpFinancialSetting> selectMpFinancialSettingList(MpFinancialSetting mpFinancialSetting)
	{
	    return mpFinancialSettingMapper.selectMpFinancialSettingList(mpFinancialSetting);
	}
	
    /**
     * 新增
     * 
     * @param mpFinancialSetting 信息
     * @return 结果
     */
	@Override
	public int insertMpFinancialSetting(MpFinancialSetting mpFinancialSetting)
	{
	    return mpFinancialSettingMapper.insertMpFinancialSetting(mpFinancialSetting);
	}
	
	/**
     * 修改
     * 
     * @param mpFinancialSetting 信息
     * @return 结果
     */
	@Override
	public int updateMpFinancialSetting(MpFinancialSetting mpFinancialSetting)
	{
	    return mpFinancialSettingMapper.updateMpFinancialSetting(mpFinancialSetting);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMpFinancialSettingByIds(String ids)
	{
		return mpFinancialSettingMapper.deleteMpFinancialSettingByIds(Convert.toStrArray(ids));
	}


	@Override
	public int changeInnerStatus(MpFinancialSetting mpFinancialSetting) {
		return mpFinancialSettingMapper.updateMpFinancialSetting(mpFinancialSetting);
	}
}
