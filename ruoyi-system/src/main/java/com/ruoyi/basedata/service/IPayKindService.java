package com.ruoyi.basedata.service;

import com.ruoyi.basedata.domain.PayKind;
import java.util.List;

/**
 *  服务层
 * 
 * @author ruoyi
 * @date 2019-05-06
 */
public interface IPayKindService 
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
     * 删除信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePayKindByIds(String ids);

	/**
	 * 修改支付类型状态
	 * @param payKind
	 * @return
	 */
    public int changeStatus(PayKind payKind);

	/**
	 * 检验名字
	 * @param payKind
	 * @return
	 */
	public String checkPaykindUnique(PayKind payKind);
}
