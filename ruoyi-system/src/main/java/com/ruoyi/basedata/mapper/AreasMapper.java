package com.ruoyi.basedata.mapper;
import com.ruoyi.basedata.domain.Areas;

import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface AreasMapper 
{
	/**
     * 查询信息
     * 
     * @param iD ID
     * @return 信息
     */
	public Areas selectAreasById(String iD);
	
	/**
     * 查询列表
     * 
     * @param areas 信息
     * @return 集合
     */
	public List<Areas> selectAreasList(Areas areas);
	
	/**
     * 新增
     * 
     * @param areas 信息
     * @return 结果
     */
	public int insertAreas(Areas areas);
	
	/**
     * 修改
     * 
     * @param areas 信息
     * @return 结果
     */
	public int updateAreas(Areas areas);
	
	/**
     * 删除
     * 
     * @param iD ID
     * @return 结果
     */
	public int deleteAreasById(String iD);
	
	/**
     * 批量删除
     * 
     * @param iDs 需要删除的数据ID
     * @return 结果
     */
	public int deleteAreasByIds(String[] iDs);
	
}