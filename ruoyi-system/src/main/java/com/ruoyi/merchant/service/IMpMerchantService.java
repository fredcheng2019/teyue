package com.ruoyi.merchant.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服务层
 *
 * @author ruoyi
 * @date 2019-05-06
 */
public interface IMpMerchantService {
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
     * @param userId
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
    public AjaxResult insertMpMerchant(MpMerchant mpMerchant);

    /**
     * 修改
     *
     * @param mpMerchant 信息
     * @return 结果
     */
    public AjaxResult updateMpMerchant(MpMerchant mpMerchant);

    /**
     * 删除信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMpMerchantByIds(String ids);

    /**
     * 修改商户信息状态
     *
     * @param mpMerchant
     * @return
     */
    int changeStatus(MpMerchant mpMerchant);

    /**
     * 修改商户预付状态
     *
     * @param mpMerchant
     * @return
     */
    int changeAdvanceStatus(MpMerchant mpMerchant);

    /**
     * 检验商户名称唯一
     *
     * @param mpMerchant
     * @return
     */
    String checkMerchantNameUnique(MpMerchant mpMerchant);

    /**
     * 更新代付通道
     *
     * @param dfChannelId
     * @param id
     * @return
     */
    AjaxResult updateMpMerchantDfChannelId(Long dfChannelId, Long id);

    String resetMerchantNo ();
}
