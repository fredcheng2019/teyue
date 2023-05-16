package com.ruoyi.basedata.service;
import com.ruoyi.basedata.domain.Areas;

import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
public interface IAreasService 
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
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteAreasByIds(String ids);


	/**
	 * 根据地区名和level获取区域
	 * @param province 地区名
	 * @param levelType	level
	 * @return			地市集合
	 */
	List<Areas> findDistrictList(String province, String levelType);

}
