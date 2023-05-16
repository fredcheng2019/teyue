package com.ruoyi.basedata.service.impl;

import java.util.List;

import com.ruoyi.basedata.domain.Areas;
import com.ruoyi.basedata.mapper.AreasMapper;
import com.ruoyi.basedata.service.IAreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 *  服务层实现
 * 
 * @author ruoyi
 * @date 2019-05-11
 */
@Service
public class AreasServiceImpl implements IAreasService
{
	@Autowired
	private AreasMapper areasMapper;

	/**
     * 查询信息
     * 
     * @param iD ID
     * @return 信息
     */
    @Override
	public Areas selectAreasById(String iD)
	{
	    return areasMapper.selectAreasById(iD);
	}
	
	/**
     * 查询列表
     * 
     * @param areas 信息
     * @return 集合
     */
	@Override
	public List<Areas> selectAreasList(Areas areas)
	{
	    return areasMapper.selectAreasList(areas);
	}
	
    /**
     * 新增
     * 
     * @param areas 信息
     * @return 结果
     */
	@Override
	public int insertAreas(Areas areas)
	{
	    return areasMapper.insertAreas(areas);
	}
	
	/**
     * 修改
     * 
     * @param areas 信息
     * @return 结果
     */
	@Override
	public int updateAreas(Areas areas)
	{
	    return areasMapper.updateAreas(areas);
	}

	/**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteAreasByIds(String ids)
	{
		return areasMapper.deleteAreasByIds(Convert.toStrArray(ids));
	}


	/**
	 * 根据地区名和level获取区域
	 *
	 * @param province 地区名
	 * @param levelType level
	 * @return 地市集合
	 */
	@Override
	public List<Areas> findDistrictList(String province, String levelType) {
		String level="";
		if ("2".equals(levelType)) {
			level="1";
		}
		if ("3".equals(levelType)) {
			level="2";
		}
		Areas district = findDistrictByShortName(province,level).get(0);
		return findDistrictByParentIdAndLevel(district.getID(), levelType);
	}

	/**
	 * 根据区域名和等级查询地市
	 */
	public List<Areas> findDistrictByShortName(String province, String levelType) {
		Areas areas = new Areas();
		areas.setProvince(province);
		areas.setLevelType(levelType);
		return areasMapper.selectAreasList(areas);
	}

	/**
	 * 根据父id和等级查询地市
	 * @param id
	 * @param levelType
	 * @return
	 */
	private List<Areas> findDistrictByParentIdAndLevel(String id, String levelType) {
		Areas areas = new Areas();
		areas.setParentId(id);
		areas.setLevelType(levelType);
		return areasMapper.selectAreasList(areas);
	}

}

