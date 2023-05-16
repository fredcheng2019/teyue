package com.ruoyi.merchant.mapper;

import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据层
 *
 * @author ruoyi
 * @date 2019-05-06
 */
public interface MpMerchantMapper {
    /**
     * 查询信息
     *
     * @param id ID
     * @return 信息
     */
    public MpMerchant selectMpMerchantById(Long id);
    /**
     * 查询信息
     *
     * @param userId ID
     * @return 信息
     */
    public MpMerchant selectMpMerchantByUserId(Long userId);

    /**
     * 查询列表
     *
     * @param mpMerchant 信息
     * @return 集合
     */
    public List<MpMerchantRSP> selectMpMerchantList(MpMerchant mpMerchant);

    /**
     * 新增
     *
     * @param mpMerchant 信息
     * @return 结果
     */
    public int insertMpMerchant(MpMerchant mpMerchant);

    /**
     * 修改
     *
     * @param mpMerchant 信息
     * @return 结果
     */
    public int updateMpMerchant(MpMerchant mpMerchant);

    /**
     * 删除
     *
     * @param id ID
     * @return 结果
     */
    public int deleteMpMerchantById(Long id);

    /**
     * 批量删除
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMpMerchantByIds(String[] ids);

    /**
     * 检验商户名称唯一
     *
     * @param name
     * @return
     */
    MpMerchant checkMerchantNameUnique(String name);

    /**
     * 检验商户账户唯一
     *
     * @param account
     * @return
     */
    int checkAccountUnique(String account);

    /**
     * 检验商户编号唯一
     * @param code
     * @return
     */
    int checkCodeUnique(String code);
    /**
     * 获取最大商户号
     * @return
     */
    String selectMaxMerchantNo();
}
