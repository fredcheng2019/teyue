package com.api.bussiness.manager.service.impl;

import com.api.bussiness.manager.bean.ManagerRspCheckWithdrawal;
import com.api.bussiness.manager.service.ManagerCheckWithdrawalService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.api.bussiness.channel.context.ChannelReqCheckWithdrawal;
import com.api.bussiness.channel.context.ChannelReqWithdrawal;
import com.api.bussiness.channel.context.ChannelRspCheckWithdrawal;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.table.finance.FinancialSetting;
import com.api.bussiness.dao.table.withdrawal.Withdrawal;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;
import com.api.bussiness.dao.withdrawal.WithdrawalDao;
import com.api.bussiness.manager.bean.ManagerReqCheckWithdrawal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;


@Service("ManagerCheckWithdrawalService")
@Transactional(rollbackFor = Exception.class)
public class ManagerCheckWithdrawalServiceImpl implements ManagerCheckWithdrawalService {

    private static Logger log = LoggerFactory.getLogger(ManagerCheckWithdrawalServiceImpl.class);

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
    ChannelDao channelDao;

    @Autowired
    MerchantDao merchantDao;

    @Override
    @Transactional
    public ManagerRspCheckWithdrawal checkWithdrawal(ManagerReqCheckWithdrawal managerReqCheckWithdrawal) throws Exception{

        ManagerRspCheckWithdrawal managerRspCheckWithdrawal = new ManagerRspCheckWithdrawal();

        Withdrawal withdrawal = null;

        if (managerReqCheckWithdrawal.getCode() != null) {
            withdrawal = withdrawalDao.getWithDrawalByCode(managerReqCheckWithdrawal.getCode());
        } else if (managerReqCheckWithdrawal.getMch_id() != null && managerReqCheckWithdrawal.getOut_trade_no() != null) {
            withdrawal = withdrawalDao.getWithDrawalByMChCodeAndMchantCode(managerReqCheckWithdrawal.getOut_trade_no(), managerReqCheckWithdrawal.getMch_id());
        }

        if (withdrawal == null) {
            log.info("查询代付{}:代付订单无效", managerReqCheckWithdrawal.getCode());
            managerRspCheckWithdrawal.setStatus(ManagerRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_KEEP);
            managerRspCheckWithdrawal.setMsg("代付订单无效");
            return managerRspCheckWithdrawal;
        }

        if (withdrawal.getStatus() == Withdrawal.STATUS_ORDER_TIME_OUT || withdrawal.getStatus() == Withdrawal.STATUS_FROZING) {
            MpChannel mpChannel = channelDao.getChannelById(withdrawal.getChannel_id());
            //向渠道发起代付订单查询
            ChannelMethod channelMethod = null;
            try {
                channelMethod = (ChannelMethod) Class.forName("com.sifang.bussiness.channel.type.impl." + withdrawal.getChannel_class_name()).newInstance();
            } catch (Exception e) {
                log.info("查询代付{}:获取支付方法失败", withdrawal.getCode());
                channelMethod = null;
            }

            if (channelMethod != null) {
                //向渠道查询订单
                ChannelReqCheckWithdrawal channelReqCheckWithdrawal = new ChannelReqCheckWithdrawal();
                channelReqCheckWithdrawal.setWithdrawal(withdrawal);
                channelReqCheckWithdrawal.setMpChannel(mpChannel);

                ChannelRspCheckWithdrawal channelRspCheckWithDrawal = getCheckRspWithdrawal(channelMethod, channelReqCheckWithdrawal);

                if (channelRspCheckWithDrawal.getStatus() == ChannelRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_SUCCESS) {
                    //再次查询，避免其他线程已经处理过了
                    Withdrawal confirmWithdrawal = null;

                    if (managerReqCheckWithdrawal.getCode() != null) {
                    	confirmWithdrawal = withdrawalDao.getWithDrawalByCode(managerReqCheckWithdrawal.getCode());
                    } else if (managerReqCheckWithdrawal.getMch_id() != null && managerReqCheckWithdrawal.getOut_trade_no() != null) {
                    	confirmWithdrawal = withdrawalDao.getWithDrawalByMChCodeAndMchantCode(managerReqCheckWithdrawal.getOut_trade_no(), managerReqCheckWithdrawal.getMch_id());
                    }
                    if (confirmWithdrawal.getStatus() != Withdrawal.STATUS_WITHDRAWAL_SUCCESS) {
                        //更新订单状态
                        Date date = new Date();
                        int result = withdrawalDao.updateWithDrawalStatusByCode(confirmWithdrawal.getCode(),channelRspCheckWithDrawal.getCh_code(), Withdrawal.STATUS_WITHDRAWAL_SUCCESS, date);
                        if(result != 1) {
                        	throw new Exception("更新代付订单异常：更新结果："+result+";订单号："+confirmWithdrawal.getCode());
                        }
                        confirmWithdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS);
                        confirmWithdrawal.setEnd(date);
                        //解冻相关钱包、扣除余额
                        if (confirmWithdrawal.getSub_fee().compareTo(channelRspCheckWithDrawal.getFee()) == 0) {//扣钱一致
                            //判断是商户代付还是渠道代付
                            if (null == confirmWithdrawal.getMerchant_code() || confirmWithdrawal.getMerchant_code().equals("")) {//平台代付
                                //更新平台钱包
                                int finResult = financeDao.updateFinanceByChannelIdWhenPlatformWithdrewSelf(confirmWithdrawal.getCh_fee(), confirmWithdrawal.getCh_fee(), confirmWithdrawal.getChannel_id());
                                if(finResult != 1) {
                                	throw new Exception("更新平台代付平台钱包异常，更新结果："+finResult+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                                //更新渠道钱包
                                int updateResult = channelWalletDao.recoveryBalanceByChIdAndWalletKindId(confirmWithdrawal.getCh_fee(), confirmWithdrawal.getChannel_id());
                                if(updateResult != 1) {
                                	throw new Exception("更新平台代付渠道钱包异常，更新结果："+updateResult+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                            } else {//商户代付
                                //增加商户流水
                               // addMerchantWalletFlow(confirmWithdrawal.getMerchant_id(), confirmWithdrawal.getWallet_kind_id(), confirmWithdrawal.getMch_fee().negate(), withdrawal.getCode());
                                //更新商户钱包
                                int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindId(confirmWithdrawal.getMch_fee(), confirmWithdrawal.getMerchant_id(), confirmWithdrawal.getWallet_kind_id());
                                if(resultMch != 1) {
                                	throw new Exception("更新商户代付商户钱包异常，更新结果："+resultMch+";商户名称："+confirmWithdrawal.getMerchant_name()+";钱包名称："+confirmWithdrawal.getWallet_kind_name());
                                }
                                //更新渠道钱包
                                int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindId(confirmWithdrawal.getCh_fee(), confirmWithdrawal.getChannel_id());
                                if(resultCh != 1) {
                                	throw new Exception("更新商户代付渠道钱包异常，更新结果："+resultCh+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                                //平台盈利增加
                                BigDecimal fin_fee = confirmWithdrawal.getFin_fee();
                                int resultFin = financeDao.updateFinance(fin_fee, confirmWithdrawal.getChannel_id());
                                if(resultFin != 1) {
                                	throw new Exception("更新商户代付平台钱包异常，更新结果："+resultFin+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                            }
                        } else {//扣钱不一致
                            log.info("提交金额和扣除金额扣除金额不一致，平台订单号:{}", confirmWithdrawal.getCode());
                            //需要重新计算钱包入账
                            
                            FinancialSetting financialSetting = financeDao.getFinancialSetting();
        					
        					//实际交易金额
        					BigDecimal real_fee = channelRspCheckWithDrawal.getFee();
        					//盈利金额
        					BigDecimal fin_fee = mpChannel.getMch_withdrawal_fee().subtract(mpChannel.getWithdrawal_fee());
        					
        					//渠道实际扣除金额
        					BigDecimal ch_real_deduction_amount;
        					//判断渠道扣款规则
        					if(mpChannel.getWithdrawal_inner() == 0) {//内扣
        						ch_real_deduction_amount = real_fee.add(mpChannel.getWithdrawal_fee());
        					}else {//外扣
        						ch_real_deduction_amount = real_fee.add(mpChannel.getWithdrawal_fee());
        					}
        					
        					//商户实际扣除金额
        					BigDecimal mch_real_deduction_amount;
        					//判断平台扣款规则
        					if(financialSetting.getWithdrawal_inner() == 0) {//内扣
        						mch_real_deduction_amount = real_fee.add(mpChannel.getMch_withdrawal_fee());
        					}else {//外扣
        						mch_real_deduction_amount = real_fee.add(mpChannel.getMch_withdrawal_fee());
        					}
        					
                            //判断是商户代付还是渠道代付
                            if (null == confirmWithdrawal.getMerchant_code() || confirmWithdrawal.getMerchant_code().equals("")) {//平台代付
                            	BigDecimal mch_fee = new BigDecimal("0");
        						fin_fee = new BigDecimal("0");
                                int resultOrder = withdrawalDao.updateWithDrawalWhenCallBackSuccessAndFeeChange(confirmWithdrawal.getCode(), real_fee, mch_fee, ch_real_deduction_amount, fin_fee);
                                if(resultOrder != 1) {
                                	throw new Exception("更新平台代付金额不一致订单异常：更新结果："+resultOrder+";订单号："+confirmWithdrawal.getCode());
                                }
                                //更新平台钱包
                                int resultFin = financeDao.updateFinanceByChannelIdWhenPlatformWithdrewSelf(confirmWithdrawal.getSub_fee(), real_fee, confirmWithdrawal.getChannel_id());
                                if(resultFin != 1) {
                                	throw new Exception("更新平台代付金额不一致平台钱包异常，更新结果："+resultFin+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                                //更新渠道钱包
                                int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdButBalNotRight(confirmWithdrawal.getSub_fee(), ch_real_deduction_amount, confirmWithdrawal.getChannel_id());
                                if(resultCh != 1) {
                                	throw new Exception("更新平台代付金额不一致渠道钱包异常，更新结果："+resultCh+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                                confirmWithdrawal.setMch_fee(mch_fee);
                            } else {//商户代付
                                int resultOrder = withdrawalDao.updateWithDrawalWhenCallBackSuccessAndFeeChange(confirmWithdrawal.getCode(), real_fee, mch_real_deduction_amount, ch_real_deduction_amount, fin_fee);
                                if(resultOrder != 1) {
                                	throw new Exception("更新商户代付金额不一致订单异常：更新结果："+resultOrder+";订单号："+confirmWithdrawal.getCode());
                                }
                                //增加商户流水
                               // addMerchantWalletFlow(confirmWithdrawal.getMerchant_id(), withdrawal.getWallet_kind_id(), mch_real_deduction_amount.negate(), withdrawal.getCode());
                                //更新商户钱包
                                int resultMch = merchantWalletDao.recoveryBalanceByMchIdAndWalletKindIdButBalNoRight(confirmWithdrawal.getMch_fee(), mch_real_deduction_amount, confirmWithdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
                                if(resultMch != 1) {
                                	throw new Exception("更新商户代付金额不一致订单异常：更新结果："+resultMch+";商户名称："+confirmWithdrawal.getMerchant_name()+";钱包名称："+confirmWithdrawal.getWallet_kind_name());
                                }
                                //更新渠道钱包
                                int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdButBalNotRight(confirmWithdrawal.getCh_fee(), ch_real_deduction_amount, confirmWithdrawal.getChannel_id());
                                if(resultCh != 1) {
                                	throw new Exception("更新商户代付渠道钱包异常，更新结果："+resultCh+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                                //平台盈利增加
                                int resultFin = financeDao.updateFinance(fin_fee, confirmWithdrawal.getChannel_id());
                                if(resultFin != 1) {
                                	throw new Exception("更新商户代付平台钱包异常，更新结果："+resultFin+";渠道名称："+confirmWithdrawal.getChannel_name());
                                }
                                confirmWithdrawal.setMch_fee(mch_real_deduction_amount);
                            }
                            confirmWithdrawal.setFin_fee(fin_fee);
                            confirmWithdrawal.setCh_fee(ch_real_deduction_amount);
                            confirmWithdrawal.setFee(real_fee);
                        }
                        log.info("代付订单代付成功{}", withdrawal.getCode());
                        managerRspCheckWithdrawal.setStatus(ManagerRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_CHANGE);
                        managerRspCheckWithdrawal.setWithdrawal(confirmWithdrawal);
                        managerRspCheckWithdrawal.setMsg("代付订单改变");
                        return managerRspCheckWithdrawal;
                    }
                } else if (channelRspCheckWithDrawal.getStatus() == ChannelRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_FAIL) {
                    //再次查询，避免其他线程已经处理过了
                    Withdrawal confirmWithdrawal = null;

                    if (managerReqCheckWithdrawal.getCode() != null) {
                    	confirmWithdrawal = withdrawalDao.getWithDrawalByCode(managerReqCheckWithdrawal.getCode());
                    } else if (managerReqCheckWithdrawal.getMch_id() != null && managerReqCheckWithdrawal.getOut_trade_no() != null) {
                    	confirmWithdrawal = withdrawalDao.getWithDrawalByMChCodeAndMchantCode(managerReqCheckWithdrawal.getOut_trade_no(), managerReqCheckWithdrawal.getMch_id());
                    }
                    if (confirmWithdrawal.getStatus() != Withdrawal.STATUS_WITHDRAWAL_FAIL) {
                        //更新订单状态
                        Date date = new Date();
                        int result = withdrawalDao.updateWithDrawalStatusByCode(confirmWithdrawal.getCode(),channelRspCheckWithDrawal.getCh_code(), Withdrawal.STATUS_WITHDRAWAL_FAIL, date);
                        if(result != 1) {
                        	throw new Exception("更新代付失败订单异常：更新结果："+result+";订单号："+confirmWithdrawal.getCode());
                        }
                        confirmWithdrawal.setEnd(date);
                        confirmWithdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_FAIL);
                        if (null == withdrawal.getMerchant_code() || withdrawal.getMerchant_code().equals("")) {//平台代付
                        	//解冻平台钱包
                        	int resultFin = financeDao.updateFinanceWhenPlatCallBackFail(withdrawal.getCh_fee(), withdrawal.getChannel_id());
                        	if(resultFin != 1) {
                        		throw new Exception("失败时更新平台代付平台钱包异常，结果："+resultFin+";渠道名称:"+withdrawal.getChannel_name());
                        	}
                            //解冻渠道钱包
                            int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdWhenFail(withdrawal.getCh_fee(), withdrawal.getChannel_id());
                            if(resultCh != 1) {
                            	throw new Exception("失败时更新平台代付渠道钱包异常，结果："+resultCh+";渠道名称:"+withdrawal.getChannel_name());
                            }
                        } else {//商户代付
                            //更新商户钱包
                            int resultMch = merchantWalletDao.recoveryBalanceByThreeIdWhenFail(withdrawal.getMch_fee(), withdrawal.getMerchant_id(), withdrawal.getWallet_kind_id());
                            if(resultMch != 1) {
                            	throw new Exception("失败时更新商户代付商户钱包异常，结果："+resultMch+";商户名称:"+withdrawal.getMerchant_name()+"钱包名称："+withdrawal.getWallet_kind_name());
                            }
                            //更新渠道钱包
                            int resultCh = channelWalletDao.recoveryBalanceByChIdAndWalletKindIdWhenFail(withdrawal.getCh_fee(), withdrawal.getChannel_id());
                            if(resultCh != 1) {
                            	throw new Exception("失败时更新商户代付渠道钱包异常，结果："+resultCh+";渠道名称:"+withdrawal.getChannel_name());
                            }
                        }

                        log.info("代付订单代付失败{}", withdrawal.getCode());
                        managerRspCheckWithdrawal.setStatus(ManagerRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_CHANGE);
                        managerRspCheckWithdrawal.setWithdrawal(confirmWithdrawal);
                        managerRspCheckWithdrawal.setMsg("代付订单改变");
                        return managerRspCheckWithdrawal;
                    }
                }
            }
        }

        log.info("查询代付订单{}:代付订单保持,", withdrawal.getCode());
        managerRspCheckWithdrawal.setStatus(ManagerRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_KEEP);
        managerRspCheckWithdrawal.setMsg("支付订单保持");
        managerRspCheckWithdrawal.setWithdrawal(withdrawal);
        return managerRspCheckWithdrawal;

    }

    @HystrixCommand(commandKey = "checkWithDrawal001",
            groupKey = "checkWithDrawalGroup",
            fallbackMethod = "checkWithDrawalTimeOutFallBack",
            ignoreExceptions = {Exception.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            })
    public ChannelRspCheckWithdrawal getCheckRspWithdrawal(ChannelMethod channelMethod, ChannelReqCheckWithdrawal req) {
        return channelMethod.checkWithdrawal(req);
    }

    public ChannelRspCheckWithdrawal checkWithDrawalTimeOutFallBack(ChannelMethod channelMethod, ChannelReqWithdrawal req) {
        ChannelRspCheckWithdrawal channelRspCheckWithdrawal = new ChannelRspCheckWithdrawal();
        channelRspCheckWithdrawal.setStatus(ChannelRspCheckWithdrawal.RSP_STATUS_WITHDRAWAL_OTHER);
        channelRspCheckWithdrawal.setMsg("查询超时");
        return channelRspCheckWithdrawal;
    }

}
