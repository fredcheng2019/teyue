package com.api.bussiness.manager.service.impl;

import com.api.bussiness.manager.service.ManagerNotifyService;
import com.api.bussiness.channel.context.ChannelReqCheckPay;
import com.api.bussiness.channel.context.ChannelRspCheckPay;
import com.api.bussiness.channel.context.ChannelRspPayNotify;
import com.api.bussiness.channel.context.ChannelRspWithdrawalNotify;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.finance.FinancialSetting;
import com.api.bussiness.dao.table.merchant.MpMerchant;
import com.api.bussiness.dao.table.merchant.MpMerchantWallet;
import com.api.bussiness.dao.table.merchant.MpMerchantWalletFlow;
import com.api.bussiness.dao.table.order.MpPay;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletFlowDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.merchant.service.MerchantNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;


@Service("ManagerNotifyService")
@Transactional(rollbackFor = Exception.class)
public class ManagerNotifyServiceImpl implements ManagerNotifyService {
    private static Logger log = LoggerFactory.getLogger(ManagerNotifyServiceImpl.class);
    @Autowired
    ChannelDao channelDao;
    @Autowired
    PayDao payDao;
    @Autowired
    WithdrawalDao withdrawalDao;
    @Autowired
    ChannelWalletDao channelWalletDao;
    @Autowired
    MerchantWalletDao merchantWalletDao;
    @Autowired
    FinanceDao financeDao;
    @Autowired
    MerchantNotifyService merchantNotifyService;
    @Autowired
    MerchantDao merchantDao;
    @Autowired
    MerchantWalletFlowDao merchantWalletFlowDao;

    @Override
    public boolean existClassName(String className) {
        MpChannel mpChannel = channelDao.getChannelByClassName(className);
        if (null == mpChannel) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public String parsePayNotify(String msg, String className) throws Exception {

        log.info("支付回调类名：{}", className);
        log.info("支付回调信息：{}", msg);
        ChannelMethod channelMethod = null;
        try {
            channelMethod = (ChannelMethod) Class.forName("com.sifang.bussiness.channel.type.impl." + className).newInstance();
        } catch (Exception e) {
            log.error("支付回调没有channelMethod异常", e);
        }
        if (null == channelMethod) {
            return "Server error";
        }
        //根据实现类查询渠道信息
        MpChannel mpChannel = channelDao.getChannelByClassName(className);
        if (null == mpChannel) {
            return "error";
        }
        ChannelRspPayNotify channelRspPayNotify = channelMethod.parsePayNotify(msg, mpChannel.getCode(), mpChannel.getSecret_key());
        //根据状态，更新支付订单状态
        if (channelRspPayNotify.getStatus() == ChannelRspPayNotify.RSP_STATUS_NOTIFY_PAY_SUCCESS) {
            //获得订单信息
            MpPay mpPay = payDao.getOrderByCode(channelRspPayNotify.getCode());
            if (null == mpPay) {
                log.info("回调订单不存在，平台订单号:{}", channelRspPayNotify.getCode());
                return "error";
            }
            // 判断订单是否超时
            if ((new Date().getTime() - mpPay.getBegin().getTime() >= 24 * 60 * 60 * 1000)) {
                log.info("订单超过24小时,不予以回调，平台订单号:{}", channelRspPayNotify.getCode());
                return "more callback time";
            }
            if (mpPay.getStatus() == MpPay.STATUS_PAY_SUCCESS) {
                log.info("回调已经通知,无需再次通知，平台订单号:{}", channelRspPayNotify.getCode());
                return "error";
            }
            String channelMethodCode = channelDao.getChannelMethodById(mpPay.getChannel_method_id());
            mpChannel.setChannelMethodCode(channelMethodCode);
            mpPay.setCh_code(channelRspPayNotify.getCh_code());
            ChannelReqCheckPay channelReqCheckPay = new ChannelReqCheckPay();
            channelReqCheckPay.setMpPay(mpPay);
            channelReqCheckPay.setMpChannel(mpChannel);
            // 到平台查询订单，看看订单是否存在
            ChannelRspCheckPay checkPay = channelMethod.checkPay(channelReqCheckPay);
            if (checkPay.getStatus() != ChannelRspCheckPay.RSP_STATUS_CHECKPAY_SUCCESS) {
                log.warn("平台订单和系统订单不一致", channelRspPayNotify.getCode());
                return "error";
            }
            if (channelRspPayNotify.getFee().compareTo(checkPay.getFee()) != 0) {
                log.warn("回调订单和查询订单金额不一致", channelRspPayNotify.getCode());
                return "error";
            }
           /* int value = Math.abs(channelRspPayNotify.getFee().intValue()-checkPay.getFee().intValue());
            if (value > 100) {
                log.warn("回调订单和查询订单金额不一致", channelRspPayNotify.getCode());
                return "error";
            }*/
            channelRspPayNotify.setFee(mpPay.getFee());
            Date endDate = new Date();
            int updateResult = payDao.updatePayByChannelPayResultWithId(mpPay.getId(), MpPay.STATUS_PAY_SUCCESS, channelRspPayNotify.getCh_code(), endDate);
            if (updateResult != 1) {
                throw new Exception("回调更新支付异常，结果：" + updateResult + ";订单号：" + channelRspPayNotify.getCode());
            }

            //提交金额和实际扣除金额一致
            if (mpPay.getSub_fee().compareTo(channelRspPayNotify.getFee()) == 0) {
                //加钱,根据商户信息是否为空来判断是商户还是平台
                if (null == mpPay.getMerchant_code() || mpPay.getMerchant_code().equals("")) {
                    //渠道钱包加钱
                    BigDecimal ch_fee = mpPay.getCh_fee();
                    int result = channelWalletDao.addMoneyByChannelId(ch_fee, mpPay.getChannel_id());
                    if (result != 1) {
                        throw new Exception("平台支付回调更新渠道钱包异常，结果：" + result + ";渠道名称：" + mpPay.getChannel_name());
                    }
                } else {

                    //商户支付 //商户钱包加钱
                    BigDecimal mch_fee = mpPay.getMch_fee();
                    //增加商户流水
                    addMerchantWalletFlow(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mch_fee, mpPay.getCode());
                    int resultMch = merchantWalletDao.addMoneyByMchIdAndWalletKindId(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mch_fee);
                    if (resultMch != 1) {
                        throw new Exception("商户支付回调更新商户钱包异常，结果：" + resultMch + ";商户名称：" + mpPay.getMerchant_name() + ";钱包名称：" + mpPay.getWallet_kind_name());
                    }
                    //渠道钱包加钱
                    BigDecimal ch_fee = mpPay.getCh_fee();
                    int resultCh = channelWalletDao.addMoneyByChannelId(ch_fee, mpPay.getChannel_id());
                    if (resultCh != 1) {
                        throw new Exception("商户支付回调更新渠道钱包异常，结果：" + resultCh + ";渠道名称：" + mpPay.getChannel_name());
                    }
                    //如果订单有代理
                    if (mpPay.getAgent_id() != null && mpPay.getAgent_id().intValue() != 0) {
                        //获取代理信息
                        MpMerchant agentMerchant = merchantDao.getMerchantByUserId(mpPay.getAgent_id());
                        // 代理钱包加钱
                        BigDecimal agent_fee = mpPay.getAgent_fee();
                        int resultAgent = merchantWalletDao.addMoneyByMchIdAndWalletKindId(agentMerchant.getId(), mpPay.getWallet_kind_id(), agent_fee);
                        if (resultAgent != 1) {
                            throw new Exception("商户支付回调更新商户钱包异常，结果：" + resultMch + ";代理名称：" + mpPay.getAgent_id() + ";钱包名称：" + mpPay.getWallet_kind_name());
                        }
                    }
                }
                //平台盈利增加
                BigDecimal fin_fee = mpPay.getFin_fee();
                int resultFin = financeDao.updateFinance(fin_fee, mpPay.getChannel_id());
                log.info("更新平台盈利钱包结果:{}", resultFin);
                if (resultFin != 1) {
                    throw new Exception("商户支付回调更新平台钱包异常，结果：" + resultFin + ";渠道名称：" + mpPay.getChannel_name());
                }
            } else {
                //提交金额和扣除金额扣除金额不一致
                log.info("支付回调-提交金额和扣除金额扣除金额不一致，平台订单号:{}", mpPay.getCode());
                //需要重新计算钱包入账
                //计算
                BigDecimal fee = channelRspPayNotify.getFee();
                BigDecimal mch_fee = fee.subtract(fee.multiply(mpPay.getMerchant_method_pay_rate()));//商户入账
                BigDecimal ch_fee = fee.subtract(fee.multiply(mpPay.getChannel_method_pay_rate()));//渠道入账
                BigDecimal fin_fee = ch_fee.subtract(mch_fee);//平台盈利入账
                //更新订单
                int updatePayWhenCallBackSuccessAndFeeChangeResult = payDao.updatePayWhenCallBackSuccessAndFeeChange(mpPay.getCode(), fee, mch_fee, ch_fee, fin_fee, channelRspPayNotify.getCh_code());
                if (updatePayWhenCallBackSuccessAndFeeChangeResult != 1) {
                    throw new Exception("更新支付回调实际支付金额不一致订单异常，结果：" + updatePayWhenCallBackSuccessAndFeeChangeResult + ";订单号:" + mpPay.getCode());
                }
                //加钱,根据商户信息是否为空来判断是商户还是平台
                if (null == mpPay.getMerchant_code() || mpPay.getMerchant_code().equals("")) {//平台支付
                    //渠道钱包加钱
                    int result = channelWalletDao.addMoneyByChannelId(ch_fee, mpPay.getChannel_id());
                    log.info("更新渠道钱包结果:{}", result);
                    if (result != 1) {
                        throw new Exception("平台支付回调金额不一致更新渠道钱包异常，结果：" + result + ";渠道名称：" + mpPay.getChannel_name());
                    }
                } else {//商户支付
                    //增加商户流水
                    addMerchantWalletFlow(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mch_fee, mpPay.getCode());
                    //商户钱包加钱
                    int resultMch = merchantWalletDao.addMoneyByMchIdAndWalletKindId(mpPay.getMerchant_id(), mpPay.getWallet_kind_id(), mch_fee);
                    log.info("更新商户钱包结果:{}", resultMch);
                    if (resultMch != 1) {
                        throw new Exception("商户支付回调金额不一致更新商户钱包异常，结果：" + resultMch + ";商户名称：" + mpPay.getMerchant_name() + ";钱包名称：" + mpPay.getWallet_kind_name());
                    }
                    //渠道钱包加钱
                    int resultCh = channelWalletDao.addMoneyByChannelId(ch_fee, mpPay.getChannel_id());
                    log.info("更新渠道钱包结果:{}", resultCh);
                    if (resultCh != 1) {
                        throw new Exception("商户支付回调金额不一致更新渠道钱包异常，结果：" + resultCh + ";渠道名称：" + mpPay.getChannel_name());
                    }
                    //如果订单有代理
                    if (mpPay.getAgent_id() != null && mpPay.getAgent_id().intValue() != 0) {
                        //获取代理信息
                        MpMerchant agentMerchant = merchantDao.getMerchantByUserId(mpPay.getAgent_id());
                        // 代理钱包加钱
                        BigDecimal agent_fee = mpPay.getAgent_fee();
                        int resultAgent = merchantWalletDao.addMoneyByMchIdAndWalletKindId(agentMerchant.getId(), mpPay.getWallet_kind_id(), agent_fee);
                        if (resultAgent != 1) {
                            throw new Exception("商户支付回调更新商户钱包异常，结果：" + resultMch + ";代理名称：" + mpPay.getAgent_id() + ";钱包名称：" + mpPay.getWallet_kind_name());
                        }
                    }
                }
                //平台盈利增加
                BigDecimal b_fin_fee = fin_fee;
                int resultFin = financeDao.updateFinance(b_fin_fee, mpPay.getChannel_id());
                log.info("更新平台盈利钱包结果:{}", resultFin);
                if (resultFin != 1) {
                    throw new Exception("商户支付回调金额不一致更新平台钱包异常，结果：" + resultFin + ";渠道名称：" + mpPay.getChannel_name());
                }
            }


            //异步通知商户-有问题-会出现end字段为空-应该是这边的事务还没有提交，异步线程就执行了（在大并发回到的时候会出现）
            mpPay.setStatus(MpPay.STATUS_PAY_SUCCESS);
            mpPay.setEnd(endDate);
            //merchantNotifyService.payNotifySync(mpPay);
            merchantNotifyService.payNotify(mpPay);
        } else if (channelRspPayNotify.getStatus() == ChannelRspPayNotify.RSP_STATUS_NOTIFY_PAY_FAIL) {
            //获得订单信息
            MpPay mpPay = payDao.getOrderByCodeForUpdate(channelRspPayNotify.getCode());
            if (null == mpPay) {
                log.info("回调订单不存在，平台订单号:{}", channelRspPayNotify.getCode());
                return "error";
            }
            Date endDate = new Date();
            int updateResult = payDao.updatePayByChannelPayResultWithEnd(channelRspPayNotify.getCode(), MpPay.STATUS_PAY_FAIL, channelRspPayNotify.getCh_code(), endDate);
            log.info("更新失败支付结果：{}", updateResult);
            if (updateResult != 1) {
                throw new Exception("更新失败支付异常，结果：" + updateResult + ";订单号：" + channelRspPayNotify.getCode());
            }
            //异步通知商户-有问题-会出现end字段为空-应该是这边的事务还没有提交，异步线程就执行了（在大并发回到的时候会出现）
            mpPay.setStatus(MpPay.STATUS_PAY_FAIL);
            mpPay.setEnd(endDate);
            //merchantNotifyService.payNotifySync(mpPay);
            merchantNotifyService.payNotify(mpPay);
        } else {
            log.info("支付回调未知");
        }
        //异步通知商户-有问题-会出现end字段为空-应该是这边的事务还没有提交，异步线程就执行了（在大并发回到的时候会出现）
        //merchantNotifyService.payNotifySync(channelRspPayNotify.getCode());
        log.info("平台订单号:{},返回上游:{}", channelRspPayNotify.getCode(),channelRspPayNotify.getRspMsg());
        return channelRspPayNotify.getRspMsg();
    }

    private void addMerchantWalletFlow( long merchant_id, long wallet_kind_id, BigDecimal fee , String order_id){
        MpMerchantWallet mpMerchantWallet = merchantWalletDao.getWalletByMerchantIdAndWalletKindId(merchant_id, wallet_kind_id);
        mpMerchantWallet = merchantWalletDao.getWalletByIdForUpdate(mpMerchantWallet.getId());
        MpMerchantWalletFlow flow = new MpMerchantWalletFlow();
        flow.setFlow_type(MpMerchantWalletFlow.FLOW_TYPE2);
        flow.setMerchant_id(Long.valueOf(mpMerchantWallet.getMerchant_id()));
        flow.setMerchant_name(merchantDao.getMerchantById(mpMerchantWallet.getMerchant_id()).getName()+"");
        flow.setPrevious_balance(mpMerchantWallet.getAccount_balance());
        flow.setAmount(fee);
        BigDecimal amount = mpMerchantWallet.getAccount_balance().add(fee);
        flow.setCurrent_balance(amount);
        flow.setCreated_time(new Date());
        flow.setCreated_by("系统");
        flow.setFlow_direction(fee.intValue() > 0 ? MpMerchantWalletFlow.FLOW_DIRECTION1 : MpMerchantWalletFlow.FLOW_DIRECTION2);
        flow.setFlow_detail_type( MpMerchantWalletFlow.FLOW_DETAIL_TYPE1 );
        flow.setOrder_id(order_id);
        merchantWalletFlowDao.addMerchantWalletFlow(flow, "");
    }

    @Override
    public String parseWithDrawalNotify(String msg, String className) throws Exception {
        log.info("代付回调类名：{}", className);
        log.info("代付回调信息：{}", msg);
        ChannelMethod channelMethod = null;
        try {
            channelMethod = (ChannelMethod) Class.forName("com.sifang.bussiness.channel.type.impl." + className).newInstance();
        } catch (Exception e) {
            log.error("代付回调异常，没有channelMethod", e);
        }
        if (null == channelMethod) {
            return "Server error";
        }
        //根据实现类查询渠道信息
        MpChannel mpChannel = channelDao.getChannelByClassName(className);
        if (null == mpChannel) {
            return "error";
        }
        ChannelRspWithdrawalNotify channelRspWithdrawalNotify = channelMethod.parseWithDrawalNotify(msg, mpChannel.getCode(), mpChannel.getSecret_key());
        //获得订单信息
        Withdrawal withdrawal = withdrawalDao.getWithDrawalByCode(channelRspWithdrawalNotify.getCode());
        //根据状态，更新支付订单状态
        if (channelRspWithdrawalNotify.getStatus() == ChannelRspWithdrawalNotify.RSP_STATUS_NOTIFY_WITHDRAWAL_SUCCESS) {
            //判断是否需要比对金额
            if (!channelRspWithdrawalNotify.isCheckFee()) {
                channelRspWithdrawalNotify.setFee(withdrawal.getSub_fee());
            }
            //解冻,先判断是否已经处理过了
            if (withdrawal.getStatus() == Withdrawal.STATUS_FROZING || withdrawal.getStatus() == Withdrawal.STATUS_ORDER_TIME_OUT) {//为处理过的
                if (withdrawal.getSub_fee().compareTo(channelRspWithdrawalNotify.getFee()) == 0) {//扣钱一致
                    //判断是商户代付还是渠道代付
                    if (null == withdrawal.getMerchant_code() || withdrawal.getMerchant_code().equals("")) {//平台代付
                        //更新平台钱包
                        int finResult = financeDao.updateFinanceByChannelIdWhenPlatformWithdrewSelf(withdrawal.getCh_fee(), withdrawal.getCh_fee(), withdrawal.getChannel_id());
                        if (finResult != 1) {
                            throw new Exception("回调更新平台代付平台钱包异常，更新结果：" + finResult + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                        //更新渠道钱包
                        int result = channelWalletDao.recoveryBalanceByChIdAndWalletKindId(withdrawal.getCh_fee(), withdrawal.getChannel_id());
                        if (result != 1) {
                            throw new Exception("回调更新平台代付渠道钱包异常，更新结果：" + result + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                    } else {//商户代付
                        //更新商户钱包
                        int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindId(withdrawal.getMch_fee(), withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
                        if (resultMch != 1) {
                            throw new Exception("回调更新商户代付商户钱包异常，更新结果：" + resultMch + ";商户名称：" + withdrawal.getMerchant_name() + ";钱包名称：" + withdrawal.getWallet_kind_name());
                        }
                        //更新渠道钱包
                        int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindId(withdrawal.getCh_fee(), withdrawal.getChannel_id());
                        if (resultCh != 1) {
                            throw new Exception("回调更新商户代付渠道钱包异常，更新结果：" + resultCh + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                        //平台盈利增加
                        BigDecimal fin_fee = withdrawal.getFin_fee();
                        int resultFin = financeDao.updateFinance(fin_fee, withdrawal.getChannel_id());
                        if (resultFin != 1) {
                            throw new Exception("回调更新商户代付平台钱包异常，更新结果：" + resultFin + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                    }
                } else {//扣钱不一致
                    log.info("代付回调-提交金额和扣除金额扣除金额不一致，平台订单号:{}", channelRspWithdrawalNotify.getCode());
                    FinancialSetting financialSetting = financeDao.getFinancialSetting();

                    //实际交易金额
                    BigDecimal real_fee = channelRspWithdrawalNotify.getFee();
                    //盈利金额
                    BigDecimal fin_fee = mpChannel.getMch_withdrawal_fee().subtract(mpChannel.getWithdrawal_fee());

                    //渠道实际扣除金额
                    BigDecimal ch_real_deduction_amount;
                    //判断渠道扣款规则
                    if (mpChannel.getWithdrawal_inner() == 0) {//内扣
                        ch_real_deduction_amount = real_fee.add(mpChannel.getWithdrawal_fee());
                    } else {//外扣
                        ch_real_deduction_amount = real_fee.add(mpChannel.getWithdrawal_fee());
                    }

                    //商户实际扣除金额
                    BigDecimal mch_real_deduction_amount;
                    //判断平台扣款规则
                    if (financialSetting.getWithdrawal_inner() == 0) {//内扣
                        mch_real_deduction_amount = real_fee.add(mpChannel.getMch_withdrawal_fee());
                    } else {//外扣
                        mch_real_deduction_amount = real_fee.add(mpChannel.getMch_withdrawal_fee());
                    }
                    //判断是商户代付还是渠道代付
                    if (null == withdrawal.getMerchant_code() || withdrawal.getMerchant_code().equals("")) {//平台代付
                        BigDecimal mch_fee = new BigDecimal("0");
                        fin_fee = new BigDecimal("0");
                        int resultOrder = withdrawalDao.updateWithDrawalWhenCallBackSuccessAndFeeChange(withdrawal.getCode(), real_fee, mch_fee, ch_real_deduction_amount, fin_fee);
                        if (resultOrder != 1) {
                            throw new Exception("回调更新平台代付金额不一致订单异常：更新结果：" + resultOrder + ";订单号：" + withdrawal.getCode());
                        }
                        //更新平台钱包
                        int finResult = financeDao.updateFinanceByChannelIdWhenPlatformWithdrewSelf(withdrawal.getSub_fee(), real_fee, withdrawal.getChannel_id());
                        if (finResult != 1) {
                            throw new Exception("回调更新平台代付金额不一致平台钱包异常，更新结果：" + finResult + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                        int resultChWallet = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdButBalNotRight(withdrawal.getSub_fee(), ch_real_deduction_amount, withdrawal.getChannel_id());
                        if (resultChWallet != 1) {
                            throw new Exception("回调更新平台代付金额不一致渠道钱包异常，更新结果：" + resultChWallet + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                    } else {//商户代付
                        int resultOrder = withdrawalDao.updateWithDrawalWhenCallBackSuccessAndFeeChange(withdrawal.getCode(), real_fee, mch_real_deduction_amount, ch_real_deduction_amount, fin_fee);
                        if (resultOrder != 1) {
                            throw new Exception("回调更新商户代付金额不一致订单异常：更新结果：" + resultOrder + ";订单号：" + withdrawal.getCode());
                        }
                        //更新商户钱包
                        int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindIdButBalNoRight(withdrawal.getMch_fee(), mch_real_deduction_amount, withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
                        if (resultMch != 1) {
                            throw new Exception("回调更新商户代付金额不一致订单异常：更新结果：" + resultMch + ";商户名称：" + withdrawal.getMerchant_name() + ";钱包名称：" + withdrawal.getWallet_kind_name());
                        }
                        //更新渠道钱包
                        int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdButBalNotRight(withdrawal.getCh_fee(), ch_real_deduction_amount, withdrawal.getChannel_id());
                        if (resultCh != 1) {
                            throw new Exception("回调更新商户代付渠道钱包异常，更新结果：" + resultCh + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                        //平台盈利增加
                        int resultFin = financeDao.updateFinance(fin_fee, withdrawal.getChannel_id());
                        if (resultFin != 1) {
                            throw new Exception("回调更新商户代付平台钱包异常，更新结果：" + resultFin + ";渠道名称：" + withdrawal.getChannel_name());
                        }
                    }

                }
            }
            int updateResult = withdrawalDao.updateWithDrawalStatusByCodeAndChCode(channelRspWithdrawalNotify.getCode(), channelRspWithdrawalNotify.getCh_code(), Withdrawal.STATUS_WITHDRAWAL_SUCCESS, new Date());
            log.info("更新成功代付结果：{}", updateResult);
            if (updateResult != 1) {
                throw new Exception("回调更新成功代付失败，结果：" + updateResult + ";订单号：" + channelRspWithdrawalNotify.getCode());
            }
        } else if (channelRspWithdrawalNotify.getStatus() == ChannelRspWithdrawalNotify.RSP_STATUS_NOTIFY_WITHDRAWAL_FAIL) {
            int result = withdrawalDao.updateWithDrawalStatusByCodeAndChCode(channelRspWithdrawalNotify.getCode(), channelRspWithdrawalNotify.getCh_code(), Withdrawal.STATUS_WITHDRAWAL_FAIL, new Date());
            if (result != 1) {
                throw new Exception("回调更新代付失败订单异常：更新结果：" + result + ";订单号：" + channelRspWithdrawalNotify.getCode());
            }
            //解冻,先判断是否已经处理过了,恢复
            if (withdrawal.getStatus() != Withdrawal.STATUS_WITHDRAWAL_FAIL && withdrawal.getStatus() != Withdrawal.STATUS_WITHDRAWAL_SUCCESS) {//
                //判断是商户代付还是渠道代付
                if (null == withdrawal.getMerchant_code() || withdrawal.getMerchant_code().equals("")) {//平台代付
                    //更新平台钱包
                    int resultFin = financeDao.updateFinanceWhenPlatCallBackFail(withdrawal.getSub_fee(), withdrawal.getChannel_id());
                    if (resultFin != 1) {
                        throw new Exception("回调失败时更新平台代付平台钱包异常，结果：" + resultFin + ";渠道名称:" + withdrawal.getChannel_name());
                    }
                    //更新渠道钱包
                    int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdWhenFail(withdrawal.getFee(), withdrawal.getChannel_id());
                    if (resultCh != 1) {
                        throw new Exception("回调失败时更新平台代付渠道钱包异常，结果：" + resultCh + ";渠道名称:" + withdrawal.getChannel_name());
                    }
                } else {//商户代付
                    //更新商户钱包
                    int resultMch = merchantWalletDao.recoveryBalanceByThreeIdWhenFail(withdrawal.getMch_fee(), withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
                    if (resultMch != 1) {
                        throw new Exception("回调失败时更新商户代付商户钱包异常，结果：" + resultMch + ";商户名称:" + withdrawal.getMerchant_name() + "钱包名称：" + withdrawal.getWallet_kind_name());
                    }
                    //更新渠道钱包
                    int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdWhenFail(withdrawal.getCh_fee(), withdrawal.getChannel_id());
                    if (resultCh != 1) {
                        throw new Exception("回调失败时更新商户代付渠道钱包异常，结果：" + resultCh + ";渠道名称:" + withdrawal.getChannel_name());
                    }
                }
            }
        } else {
            log.info("代付回调未知");
        }
        return channelRspWithdrawalNotify.getRspMsg();

    }

}
