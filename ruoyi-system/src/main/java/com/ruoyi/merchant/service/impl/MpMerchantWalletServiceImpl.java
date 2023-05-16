package com.ruoyi.merchant.service.impl;

import com.ruoyi.basedata.mapper.WalletKindMapper;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.financial.mapper.FinancialWalletMapper;
import com.ruoyi.merchant.domain.*;
import com.ruoyi.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.merchant.mapper.MpMerchantMapper;
import com.ruoyi.merchant.mapper.MpMerchantWalletMapper;
import com.ruoyi.merchant.service.IMpMerchantWalletService;
import com.ruoyi.system.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 服务层实现
 *
 * @author ruoyi
 * @date 2019-05-06
 */
@Service
public class MpMerchantWalletServiceImpl implements IMpMerchantWalletService {
    Logger log = LoggerFactory.getLogger(MpMerchantWalletServiceImpl.class);
    @Autowired
    private MpMerchantWalletMapper mpMerchantWalletMapper;
    @Autowired
    private MpMerchantMapper merchantMapper;
    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;
    @Autowired
    private FinancialWalletMapper financialWalletMapper;


    /**
     * 查询信息
     *
     * @param id ID
     * @return 信息
     */
    @Override
    public MpMerchantWallet selectMpMerchantWalletById(Integer id) {
        return mpMerchantWalletMapper.selectMpMerchantWalletById(id);
    }

    /**
     * 查询列表
     *
     * @param mpMerchantWallet 信息
     * @return 集合
     */
    @Override
    public List<MpMerchantWalletVo> selectMpMerchantWalletList(MpMerchantWallet mpMerchantWallet) {
        return mpMerchantWalletMapper.selectMpMerchantWalletList(mpMerchantWallet);
    }

    @Override
    public MpMerchantWalletVo selectStatistics(MpMerchantWallet mpMerchantWallet) {
        return mpMerchantWalletMapper.selectStatistics(mpMerchantWallet);
    }

    /**
     * 新增
     *
     * @param mpMerchantWallet 信息
     * @return 结果
     */
    @Override
    public int insertMpMerchantWallet(MpMerchantWallet mpMerchantWallet) {
        return mpMerchantWalletMapper.insertMpMerchantWallet(mpMerchantWallet);
    }

    /**
     * 修改
     *
     * @param mpMerchantWallet 信息
     * @return 结果
     */
    @Override
    public int updateMpMerchantWallet(MpMerchantWallet mpMerchantWallet) {
        return mpMerchantWalletMapper.updateMpMerchantWallet(mpMerchantWallet);
    }

    @Override
    @Transactional
    public int updateAdvanceBalance(SysUser user, MpMerchantWallet mpMerchantWallet) {
        MpMerchantWallet wallet = mpMerchantWalletMapper.selectMpMerchantWalletByIdForUpdate(mpMerchantWallet.getId());
        mpMerchantWallet.setAdvanceBalance(mpMerchantWallet.getAdvanceBalance().multiply(new BigDecimal(100)).add(wallet.getAdvanceBalance()));

        MerchantWalletFlow flow = new MerchantWalletFlow();
        flow.setFlowType(MerchantWalletFlow.FLOW_TYPE1);
        flow.setMerchantId(Long.valueOf(mpMerchantWallet.getMerchantId()));
        flow.setMerchantName(merchantMapper.selectMpMerchantById(Long.valueOf(mpMerchantWallet.getMerchantId())).getName());
        flow.setPreviousBalance(wallet.getAdvanceBalance());
        BigDecimal amount = mpMerchantWallet.getAdvanceBalance().subtract(wallet.getAdvanceBalance());
        flow.setAmount(amount);
        flow.setCurrentBalance(mpMerchantWallet.getAdvanceBalance());
        flow.setCreatedBy(user.getLoginName());
        flow.setOrderId(mpMerchantWallet.getRemark());
        flow.setCreatedTime(new Date());
        flow.setFlowDirection(amount.intValue() > 0 ? MerchantWalletFlow.FLOW_DIRECTION1 : MerchantWalletFlow.FLOW_DIRECTION2);
        flow.setFlowDetailType(amount.intValue() > 0 ? MerchantWalletFlow.FLOW_DETAIL_TYPE3 : MerchantWalletFlow.FLOW_DETAIL_TYPE4);
        merchantWalletFlowMapper.insertMerchantWalletFlow(flow);
        return mpMerchantWalletMapper.updateMpMerchantWallet(mpMerchantWallet);
    }

    @Override
    @Transactional
    public int updateAccountBalance(SysUser user, MpMerchantWallet mpMerchantWallet) {
        MpMerchantWallet wallet = mpMerchantWalletMapper.selectMpMerchantWalletByIdForUpdate(mpMerchantWallet.getId());
        mpMerchantWallet.setAccountBalance(mpMerchantWallet.getAccountBalance().multiply(new BigDecimal(100)).add(wallet.getAccountBalance()));

        MerchantWalletFlow flow = new MerchantWalletFlow();
        flow.setOrderId(mpMerchantWallet.getRemark());
        flow.setFlowType(MerchantWalletFlow.FLOW_TYPE2);
        flow.setMerchantId(Long.valueOf(mpMerchantWallet.getMerchantId()));
        flow.setMerchantName(merchantMapper.selectMpMerchantById(Long.valueOf(mpMerchantWallet.getMerchantId())).getName());
        flow.setPreviousBalance(wallet.getAccountBalance());
        BigDecimal amount = mpMerchantWallet.getAccountBalance().subtract(wallet.getAccountBalance());
        flow.setAmount(amount);
        flow.setCurrentBalance(mpMerchantWallet.getAccountBalance());
        flow.setCreatedBy(user.getLoginName());
        flow.setCreatedTime(new Date());
        flow.setFlowDirection(amount.intValue() > 0 ? MerchantWalletFlow.FLOW_DIRECTION1 : MerchantWalletFlow.FLOW_DIRECTION2);
        flow.setFlowDetailType(amount.intValue() > 0 ? MerchantWalletFlow.FLOW_DETAIL_TYPE3 : MerchantWalletFlow.FLOW_DETAIL_TYPE4);
        merchantWalletFlowMapper.insertMerchantWalletFlow(flow);
        return mpMerchantWalletMapper.updateMpMerchantWallet(mpMerchantWallet);
    }

    @Override
    public int updateFrozenBalance(SysUser user, MpMerchantWallet mpMerchantWallet) {
        MpMerchantWallet wallet = mpMerchantWalletMapper.selectMpMerchantWalletByIdForUpdate(mpMerchantWallet.getId());
        if(mpMerchantWallet.getFrozenBalance()==null){
            return 0;
        }
        if(wallet.getFrozenBalance()==null || wallet.getFrozenBalance().intValue()==0){
            wallet.setFrozenBalance(new BigDecimal(0));
        }
        BigDecimal amount = mpMerchantWallet.getFrozenBalance().multiply(new BigDecimal(100));
        MerchantWalletFlow flow = new MerchantWalletFlow();
        flow.setOrderId(mpMerchantWallet.getRemark());
        flow.setPreviousBalance(wallet.getAccountBalance());

        //解冻
        if(amount.intValue()>0){
            if(wallet.getFrozenBalance().intValue()-amount.intValue()<0){
                return 0;
            }
            wallet.setFrozenBalance(wallet.getFrozenBalance().subtract(amount));
            wallet.setAccountBalance(amount.add(wallet.getAccountBalance()));
        }
        //冻洁
        if(amount.intValue()<0){
            wallet.setAccountBalance(amount.add(wallet.getAccountBalance()));
           /* if(wallet.getAccountBalance().intValue()<0){
                return 0;
            }*/
            wallet.setFrozenBalance(wallet.getFrozenBalance().subtract(amount));
        }
        flow.setFlowType(MerchantWalletFlow.FLOW_TYPE2);
        flow.setMerchantId(Long.valueOf(wallet.getMerchantId()));
        flow.setMerchantName(merchantMapper.selectMpMerchantById(Long.valueOf(wallet.getMerchantId())).getName());
        flow.setAmount(amount);
        flow.setCurrentBalance(wallet.getAccountBalance());
        flow.setCreatedBy(user.getLoginName());
        flow.setCreatedTime(new Date());
        flow.setFlowDirection(amount.intValue() > 0 ? MerchantWalletFlow.FLOW_DIRECTION1 : MerchantWalletFlow.FLOW_DIRECTION2);
        flow.setFlowDetailType(amount.intValue() > 0 ? MerchantWalletFlow.FLOW_DETAIL_TYPE8 : MerchantWalletFlow.FLOW_DETAIL_TYPE7);
        merchantWalletFlowMapper.insertMerchantWalletFlow(flow);
        return mpMerchantWalletMapper.updateMpMerchantWallet(wallet);
    }

    /**
     * 删除对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteMpMerchantWalletByIds(String ids) {
        return mpMerchantWalletMapper.deleteMpMerchantWalletByIds(Convert.toStrArray(ids));
    }

    @Override
    public MpMerchantWallet checkMerchantWallet(MpMerchantWallet mpMerchantWallet) {
        return mpMerchantWalletMapper.checkMerchantWallet(mpMerchantWallet);
    }


    /**
     * 查询渠道钱包
     *
     * @param status 状态
     * @return 渠道钱包相关信息集合
     */
    @Override
    public List<MerchantWithdrawal> channelWalletList(int status) {
        return mpMerchantWalletMapper.channelWalletList(status);
    }


    @Override
    public MpMerchantWalletVo selectMpMerchantWalletVoById(Integer id) {
        MpMerchantWalletVo mpMerchantWalletVo = mpMerchantWalletMapper.selectMpMerchantWalletVoById(id);
        if (mpMerchantWalletVo==null) {
            mpMerchantWalletVo = mpMerchantWalletMapper.selectMpMerchantWalletVoByMerchantId(id);
        }
        return mpMerchantWalletVo;
    }

    @Override
    public MpMerchantWalletVo selectMpMerchantWalletVoByMerchantId(Integer id) {
        MpMerchantWalletVo mpMerchantWalletVo =  mpMerchantWalletMapper.selectMpMerchantWalletVoByMerchantId(id);
        return mpMerchantWalletVo;
    }

    /**
     * 更新余额
     *
     * @param merchantId
     * @param walletKindId
     * @param mchFee
     * @return
     */
    @Override
    public int updateBalance(Long merchantId, Integer walletKindId, BigDecimal mchFee) {
        return mpMerchantWalletMapper.updateBalance(merchantId, walletKindId, mchFee);
    }

}
