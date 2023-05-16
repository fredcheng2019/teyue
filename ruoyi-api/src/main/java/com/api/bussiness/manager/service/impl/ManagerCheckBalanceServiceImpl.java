package com.api.bussiness.manager.service.impl;

import java.util.Date;

import com.api.bussiness.manager.service.ManagerCheckBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.bussiness.channel.context.ChannelReqCheckBalance;
import com.api.bussiness.channel.context.ChannelRspCheckBalance;
import com.api.bussiness.channel.type.ChannelMethod;
import com.api.bussiness.dao.order.ChannelDao;
import com.api.bussiness.dao.table.channel.MpChannel;
import com.api.bussiness.dao.wallet.ChannelWalletDao;
import com.api.bussiness.manager.bean.ManagerReqCheckBalance;
import com.api.bussiness.manager.bean.ManagerRspCheckBalance;


@Service("ManagerCheckBalanceService")
public class ManagerCheckBalanceServiceImpl implements ManagerCheckBalanceService {

    private static Logger log = LoggerFactory.getLogger(ManagerCheckBalanceServiceImpl.class);

    @Autowired
    ChannelDao channelDao;

    @Autowired
    ChannelWalletDao channelWalletDao;

    @Override
    public ManagerRspCheckBalance checkBalance(ManagerReqCheckBalance managerReqCheckBalance) {
        ManagerRspCheckBalance result = new ManagerRspCheckBalance();
        MpChannel mpChannel = channelDao.getChannelByCode(managerReqCheckBalance.getChannel_code());
        if (null == mpChannel) {
            result.setStatus(ManagerRspCheckBalance.RSP_STATUS_FAIL);
            result.setMsg("渠道不存在");
            return result;
        }
        //向渠道发起余额查询
        ChannelMethod channelMethod = null;
        try {
            channelMethod = (ChannelMethod) Class.forName("com.sifang.bussiness.channel.type.impl." + mpChannel.getClass_name()).newInstance();
        } catch (Exception e) {
            log.error("请查询单异常,", e);
        }
        //判断
        if (null == channelMethod) {
            result.setStatus(ManagerRspCheckBalance.RSP_STATUS_FAIL);
            log.info("没找到channelMethod");
            result.setMsg("系统异常");
            return result;
        }
        ChannelReqCheckBalance channelReqCheckBalance = new ChannelReqCheckBalance();
        channelReqCheckBalance.setMpChannel(mpChannel);
        ChannelRspCheckBalance channelRspCheckBalance = channelMethod.checkBalance(channelReqCheckBalance);
        if (channelRspCheckBalance.getStatus() == ChannelRspCheckBalance.RSP_STATUS_CHECKBALANCE_SUCCESS) {
            //更新查询时间
            int updateResult = channelWalletDao.updateCheckDateAndBalanceByChannelId(channelRspCheckBalance.getAvaiable(), new Date(), mpChannel.getId());
            log.info("更新渠道查询余额时间结果：{}", updateResult);

            result.setStatus(ManagerRspCheckBalance.RSP_STATUS_BALANCE_SUCCESS);
            result.setMsg("成功");
            result.setBalance(channelRspCheckBalance.getBalance());
        } else {
            result.setStatus(ManagerRspCheckBalance.RSP_STATUS_FAIL);
            result.setMsg(channelRspCheckBalance.getMsg());
        }
        return result;
    }

}
