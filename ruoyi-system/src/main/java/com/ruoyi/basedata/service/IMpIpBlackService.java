package com.ruoyi.basedata.service;

import com.ruoyi.basedata.domain.MpIpBlack;
import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-15
 */
public interface IMpIpBlackService 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public MpIpBlack selectMpIpBlackById(Long id);
	
	/**
     * 查询列表
     * 
     * @param mpIpBlack 信息
     * @return 集合
     */
	public List<MpIpBlack> selectMpIpBlackList(MpIpBlack mpIpBlack);
	
	/**
     * 新增
     * 
     * @param mpIpBlack 信息
     * @return 结果
     */
	public int insertMpIpBlack(MpIpBlack mpIpBlack);
	
	/**
     * 修改
     * 
     * @param mpIpBlack 信息
     * @return 结果
     */
	public int updateMpIpBlack(MpIpBlack mpIpBlack);
		
	/**
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteMpIpBlackByIds(String ids);
	
}
