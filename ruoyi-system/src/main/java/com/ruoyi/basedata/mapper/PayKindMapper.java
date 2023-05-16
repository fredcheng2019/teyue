package com.ruoyi.basedata.mapper;

import com.ruoyi.basedata.domain.PayKind;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  数据层
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public interface PayKindMapper 
{
	/**
     * 查询信息
     * 
     * @param id ID
     * @return 信息
     */
	public PayKind selectPayKindById(Long id);
	
	/**
     * 查询列表
     * 
     * @param payKind 信息
     * @return 集合
     */
	public List<PayKind> selectPayKindList(PayKind payKind);
	
	/**
     * 新增
     * 
     * @param payKind 信息
     * @return 结果
     */
	public int insertPayKind(PayKind payKind);
	
	/**
     * 修改
     * 
     * @param payKind 信息
     * @return 结果
     */
	public int updatePayKind(PayKind payKind);
	
	/**
     * 删除
     * 
     * @param id ID
     * @return 结果
     */
	public int deletePayKindById(Integer id);
	
	/**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePayKindByIds(String[] ids);

	/**
	 * 检测支付类型名称唯一
	 * @param name
	 * @return
	 */
	PayKind checkPaykindUnique(String name);


	/**
	 * 根据ids查询支付类型
	 * @param payIds
	 * @return
	 */
    List<PayKind> selectPayKindListByIds(@Param("payIds") Long[] payIds);


}