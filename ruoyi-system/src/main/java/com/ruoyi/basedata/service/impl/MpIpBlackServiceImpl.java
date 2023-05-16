package com.ruoyi.basedata.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.basedata.mapper.MpIpBlackMapper;
import com.ruoyi.basedata.domain.MpIpBlack;
import com.ruoyi.basedata.service.IMpIpBlackService;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-15
 */
@Service
public class MpIpBlackServiceImpl implements IMpIpBlackService 
{
	@Autowired
	private MpIpBlackMapper mpIpBlackMapper;

	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
    @Override
	public MpIpBlack selectMpIpBlackById(Long id)
	{
	    return mpIpBlackMapper.selectMpIpBlackById(id);
	}
	
	/**
     * 查询列表
     * 
     * @param mpIpBlack 信息
     * @return 集合
     */
	@Override
	public List<MpIpBlack> selectMpIpBlackList(MpIpBlack mpIpBlack)
	{
	    return mpIpBlackMapper.selectMpIpBlackList(mpIpBlack);
	}
	
    /**
     * 新增
     * 
     * @param mpIpBlack 信息
     * @return 结果
     */
	@Override
	public int insertMpIpBlack(MpIpBlack mpIpBlack)
	{
	    return mpIpBlackMapper.insertMpIpBlack(mpIpBlack);
	}
	
	/**
     * 修改
     * 
     * @param mpIpBlack 信息
     * @return 结果
     */
	@Override
	public int updateMpIpBlack(MpIpBlack mpIpBlack)
	{
	    return mpIpBlackMapper.updateMpIpBlack(mpIpBlack);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteMpIpBlackByIds(String ids)
	{
		return mpIpBlackMapper.deleteMpIpBlackByIds(Convert.toStrArray(ids));
	}
	
}
