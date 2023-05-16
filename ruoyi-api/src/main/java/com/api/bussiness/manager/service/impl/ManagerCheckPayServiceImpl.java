package com.api.bussiness.manager.service.impl;

import com.api.bussiness.manager.bean.ManagerReqCheckPay;
import com.api.bussiness.manager.bean.ManagerRspCheckPay;
import com.api.bussiness.manager.service.ManagerCheckPayService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.api.bussiness.channel.context.ChannelReqCheckPay;
import com.api.bussiness.channel.context.ChannelRspCheckPay;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.dao.finance.FinanceDao;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.order.MpPay;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.dao.wallet.MerchantWalletDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("ManagerCheckPayService")
@Transactional(rollbackFor = Exception.class)
public class ManagerCheckPayServiceImpl implements ManagerCheckPayService {

    private static Logger log = LoggerFactory.getLogger(ManagerCheckPayServiceImpl.class);

    @Resource
    PayDao payDao;
    @Resource
    ChannelWalletDao channelWalletDao;
    @Resource
    MerchantWalletDao merchantWalletDao;
    @Resource
    FinanceDao financeDao;
    @Resource
    ChannelDao channelDao;

    @Override
    public ManagerRspCheckPay checkPay(ManagerReqCheckPay managerReqCheckPay) throws Exception{
        ManagerRspCheckPay managerRspCheckPay = new ManagerRspCheckPay();

        MpPay mpPay = null;
        if (managerReqCheckPay.getCode() != null) {
            //内部请求
            mpPay = payDao.getOrderByCode(managerReqCheckPay.getCode());
        } else if (managerReqCheckPay.getMerchant_code() != null && managerReqCheckPay.getMch_code() != null) {
            //外部请求
            mpPay = payDao.getPayByMchIdAndMchCode(managerReqCheckPay.getMerchant_code(), managerReqCheckPay.getMch_code());
        }

        if (null == mpPay) {
            log.info("查询支付{}:支付订单无效", managerReqCheckPay.getCode());
            managerRspCheckPay.setStatus(ManagerRspCheckPay.RSP_STATUS_PAY_KEEP);
            managerRspCheckPay.setMsg("支付订单无效");
            return managerRspCheckPay;
        }

        if (mpPay.getStatus() == MpPay.STATUS_WAIT_TO_PAY) {/*
            //等待支付的订单，向渠道发起支付订单查询
            //获取支付方法句柄
            ChannelMethod channelMethod = null;
            try {
                channelMethod = (ChannelMethod) Class.forName("com.sifang.bussiness.channel.type.impl." + mpPay.getChannel_class_name()).newInstance();
            } catch (Exception e) {
                log.info("查询支付{}:获取支付方法失败", mpPay.getCode());
                channelMethod = null;
            }
            if (channelMethod != null) {
                //获取渠道信息
                MpChannel mpChannel = channelDao.getChannelById(mpPay.getChannel_id());
                ChannelReqCheckPay channelReqCheckPay = new ChannelReqCheckPay();
                channelReqCheckPay.setMpPay(mpPay);
                channelReqCheckPay.setMpChannel(mpChannel);
                //请求渠道订单状态（网络）
                ChannelRspCheckPay channelRspCheckPay = getChannelRspCheckPay(channelMethod, channelReqCheckPay);

                if (channelRspCheckPay.getStatus() == ChannelRspCheckPay.RSP_STATUS_CHECKPAY_SUCCESS) {
                    //避免其它线程处理 （重新查询）
                	MpPay confirmMpPay = null;
                    if (managerReqCheckPay.getCode() != null) {
                        //内部请求
                    	confirmMpPay = payDao.getOrderByCode(managerReqCheckPay.getCode());
                    } else if (managerReqCheckPay.getMerchant_code() != null && managerReqCheckPay.getMch_code() != null) {
                        //外部请求
                    	confirmMpPay = payDao.getPayByMchIdAndMchCode(managerReqCheckPay.getMerchant_code(), managerReqCheckPay.getMch_code());
                    }
                    if (confirmMpPay.getStatus() != MpPay.STATUS_PAY_SUCCESS) {
                        //更新支付订单状态
                        Date date = new Date();
                        confirmMpPay.setStatus(MpPay.STATUS_PAY_SUCCESS);
                        confirmMpPay.setEnd(date);

                        log.info("支付订单支付成功{}", confirmMpPay.getCode());
                        managerRspCheckPay.setStatus(ManagerRspCheckPay.RSP_STATUS_PAY_CHANGE);
                        managerRspCheckPay.setMsg("支付订单改变");
                        managerRspCheckPay.setMpPay(confirmMpPay);
                        return managerRspCheckPay;
                    }
                } else if (channelRspCheckPay.getStatus() == ChannelRspCheckPay.RSP_STATUS_CHECKPAY_FAIL) {
                    //避免其它线程处理 （重新查询）
                    MpPay confirmMpPay = null;
                    if (managerReqCheckPay.getCode() != null) {
                        //内部请求
                    	confirmMpPay = payDao.getOrderByCode(managerReqCheckPay.getCode());
                    } else if (managerReqCheckPay.getMerchant_code() != null && managerReqCheckPay.getMch_code() != null) {
                        //外部请求
                    	confirmMpPay = payDao.getPayByMchIdAndMchCode(managerReqCheckPay.getMerchant_code(), managerReqCheckPay.getMch_code());
                    }

                    if (confirmMpPay.getStatus() != MpPay.STATUS_PAY_FAIL) {
                        //更新支付订单状态
                        Date date = new Date();
                        int result = payDao.updatePayStatusEndByCode(confirmMpPay.getCode(), MpPay.STATUS_PAY_FAIL, new Date(),confirmMpPay.getCh_code());
                        if(result != 1) {
                        	throw new Exception("更新支付订单异常：更新结果："+result+";订单号："+confirmMpPay.getCode());
                        }
                        confirmMpPay.setStatus(MpPay.STATUS_PAY_FAIL);
                        confirmMpPay.setEnd(date);

                        log.info("支付订单支付失败{}", confirmMpPay.getCode());
                        managerRspCheckPay.setStatus(ManagerRspCheckPay.RSP_STATUS_PAY_CHANGE);
                        managerRspCheckPay.setMsg("支付订单改变");
                        managerRspCheckPay.setMpPay(confirmMpPay);
                        return managerRspCheckPay;
                    }
                }
            }*/
        }

        log.info("查询支付订单{}:支付订单保持,状态{}", mpPay.getCode(), mpPay.getStatus());
        managerRspCheckPay.setStatus(ManagerRspCheckPay.RSP_STATUS_PAY_KEEP);
        managerRspCheckPay.setMsg("支付订单保持");
        managerRspCheckPay.setMpPay(mpPay);
        return managerRspCheckPay;
    }

    @HystrixCommand(commandKey = "checkPay001",
            groupKey = "checkPayGroup",
            fallbackMethod = "getChannelRspCheckPayFallBack",
            ignoreExceptions = {Exception.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
            })
    public ChannelRspCheckPay getChannelRspCheckPay(ChannelMethod channelMethod, ChannelReqCheckPay req) {
        return channelMethod.checkPay(req);
    }

    public ChannelRspCheckPay getChannelRspCheckPayFallBack(ChannelMethod channelMethod, ChannelReqCheckPay req) {
        ChannelRspCheckPay order = new ChannelRspCheckPay();
        order.setStatus(ChannelRspCheckPay.RSP_STATUS_CHECKPAY_OTHER);
        return order;
    }
}
