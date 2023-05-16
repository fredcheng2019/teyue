package com.ruoyi.merchant.service.impl;

import com.ruoyi.basedata.domain.WalletKind;
import com.ruoyi.basedata.mapper.WalletKindMapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantMethod;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import com.ruoyi.merchant.domain.MpMerchantWallet;
import com.ruoyi.merchant.mapper.MpMerchantMapper;
import com.ruoyi.merchant.mapper.MpMerchantMethodMapper;
import com.ruoyi.merchant.mapper.MpMerchantWalletMapper;
import com.ruoyi.merchant.service.IMpMerchantService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务层实现
 *
 * @author ruoyi
 * @date 2019-05-06
 */
@Service
public class MpMerchantServiceImpl implements IMpMerchantService {
    Logger logger = LoggerFactory.getLogger(MpMerchantServiceImpl.class);

    @Autowired
    private MpMerchantMapper mpMerchantMapper;
    @Autowired
    private MpMerchantMethodMapper merchantMethodMapper;
    @Autowired
    private WalletKindMapper walletKindMapper;
    @Autowired
    private MpMerchantWalletMapper merchantWalletMapper;
    @Autowired
    private SysUserMapper userMapper;


    /**
     * 查询信息
     *
     * @param id ID
     * @return 信息
     */
    @Override
    public MpMerchant selectMpMerchantById(Long id) {
        return mpMerchantMapper.selectMpMerchantById(id);
    }

    @Override
    public MpMerchant selectMpMerchantByUserId(Long userId) {
        return mpMerchantMapper.selectMpMerchantByUserId(userId);
    }

    /**
     * 查询列表
     *
     * @param mpMerchant 信息
     * @return 集合
     */
    @Override
    public List<MpMerchantRSP> selectMpMerchantList(MpMerchant mpMerchant) {
        return mpMerchantMapper.selectMpMerchantList(mpMerchant);
    }


    /**
     * 新增
     *
     * @param mpMerchant 信息
     * @return 结果
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    public AjaxResult insertMpMerchant(MpMerchant mpMerchant) {
        try {
            // 一个用户只能添加一个商户
            MpMerchant merchantAdd = new MpMerchant();
            merchantAdd.setUserId(mpMerchant.getUserId());
            // 判断商户是否有关联用户
            List<MpMerchantRSP> merchantRSPList = mpMerchantMapper.selectMpMerchantList(merchantAdd);
            if (merchantRSPList.size() > 0) {
                return AjaxResult.error("已有商户(" + merchantRSPList.get(0).getName() + ")关联所属用户，一个商户只能关联一个用户");
            }
            if(mpMerchantMapper.checkCodeUnique(mpMerchant.getCode())>0){
                return AjaxResult.error("商户编号(" + mpMerchant.getCode() + ")已被使用,请重置");
            }
            // 新增商户
            mpMerchantMapper.insertMpMerchant(mpMerchant);
            return AjaxResult.success("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加商户异常！请联系管理员!", e);
            return AjaxResult.error("添加商户异常！请联系管理员!");
        }
    }

    /**
     * 修改
     *
     * @param mpMerchant 信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateMpMerchant(MpMerchant mpMerchant) {
        // 更新商户支付方法的商户信息
        MpMerchantMethod merchantMethod = new MpMerchantMethod();
        merchantMethod.setMerchantId(mpMerchant.getId());
        List<MpMerchantMethod> merchantMethodList = merchantMethodMapper.selectMpMerchantMethodList(merchantMethod);
        if (merchantMethodList.size() > 0) {
            for (MpMerchantMethod merchantMethodUpdate : merchantMethodList) {
                merchantMethodUpdate.setMerchantName(mpMerchant.getName());
                merchantMethodUpdate.setMerchantCode(mpMerchant.getCode());
                merchantMethodUpdate.setMerchantStatus(mpMerchant.getStatus());
                // 更新商户支付方法
                merchantMethodMapper.updateMpMerchantMethod(merchantMethodUpdate);
            }
        }
        // 一个商户只能关联一个用户
        /*MpMerchant merchantEdit = new MpMerchant();
        merchantEdit.setUserId(mpMerchant.getUserId());
        merchantEdit.setAgentId(null);
        List<MpMerchantRSP> merchantRSPList = mpMerchantMapper.selectMpMerchantList(merchantEdit);
        if (merchantRSPList.size() >1) {
            MpMerchantRSP merchantRSP = merchantRSPList.get(0);
            if (!merchantRSP.getId().equals(mpMerchant.getId())) {
                return AjaxResult.error("已有商户(" + merchantRSP.getName() + ")关联所属用户，一个商户只能关联一个用户！");
            }
        }*/
        mpMerchantMapper.updateMpMerchant(mpMerchant);
        //只要谷歌验证码不为空，就去更新数据库得用户信息
        if (StringUtils.isNotEmpty(mpMerchant.getGoogleSecret())) {
            MpMerchant temp = mpMerchantMapper.selectMpMerchantById(mpMerchant.getId());
            SysUser user = new SysUser();
            user.setGoogleSecret(mpMerchant.getGoogleSecret());
            user.setUserId(temp.getUserId());
            userMapper.updateUser(user);
        }
        return AjaxResult.success("修改成功！");
    }

    /**
     * 更新代付通道
     *
     * @param dfChannelId
     * @param id
     * @return
     */
    @Override
    public AjaxResult updateMpMerchantDfChannelId(Long dfChannelId, Long id) {
        MpMerchant mpMerchant = new MpMerchant();
        mpMerchant.setId(id);
        mpMerchant.setDfChannelId(dfChannelId);
        mpMerchantMapper.updateMpMerchant(mpMerchant);
        return AjaxResult.success("修改成功！");
    }

    /**
     * 删除对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMpMerchantByIds(String ids) {
        int delnums = 0;
        String[] split = ids.split(",");
        //这里我们遍历所有数据
        for (String s : split) {
            if (StringUtils.isNotBlank(s)) {
                MpMerchant mpMerchant = selectMpMerchantById(Long.valueOf(s));
                userMapper.deleteUserById(mpMerchant.getUserId());
                mpMerchantMapper.deleteMpMerchantById(mpMerchant.getId());
                delnums++;
            }
        }
        return delnums;
    }

    /**
     * 修改商户信息状态
     *
     * @param mpMerchant
     * @return
     */
    @Override
    public int changeStatus(MpMerchant mpMerchant) {
        // 查询商户支付方法   修改商户的状态字段
        MpMerchantMethod merchantMethod = new MpMerchantMethod();
        merchantMethod.setMerchantId(mpMerchant.getId());
        List<MpMerchantMethod> merchantMethodListExist = merchantMethodMapper.selectMpMerchantMethodList(merchantMethod);
        if (merchantMethodListExist.size() > 0) {
            for (MpMerchantMethod merchantMethodExist : merchantMethodListExist) {
                merchantMethodExist.setMerchantStatus(mpMerchant.getStatus());
                // 更新
                merchantMethodMapper.updateMpMerchantMethod(merchantMethodExist);
            }
        }
        return mpMerchantMapper.updateMpMerchant(mpMerchant);
    }

    /**
     * 修改商户预付状态
     *
     * @param mpMerchant
     * @return
     */
    @Override
    public int changeAdvanceStatus(MpMerchant mpMerchant) {
        return mpMerchantMapper.updateMpMerchant(mpMerchant);
    }

    /**
     * 检验商户名称唯一
     */
    @Override
    public String checkMerchantNameUnique(MpMerchant mpMerchant) {
        Long mpMerchantId = StringUtils.isNull(mpMerchant.getId()) ? -1L : mpMerchant.getId();
        MpMerchant info = mpMerchantMapper.checkMerchantNameUnique(mpMerchant.getName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != mpMerchantId.longValue()) {
            return "1";
        }
        return "0";
    }


    /**
     * 新增的商户添加所有的渠道支付方法
     */
    public List<MpMerchantMethod> addMerchantMethod(Long agentId, MpMerchant merchant, BigDecimal payRate) {

        return null;
    }

    /**
     * 商户号在10000~99999递增
     * @return
     */
    @Override
    public String resetMerchantNo() {
       String max = mpMerchantMapper.selectMaxMerchantNo();
       if(StringUtils.isBlank(max)){
           max = "10000";
       }
       String merchantNo=(Integer.valueOf(max)+1)+"";
       return merchantNo;
    }
}
