package com.ruoyi.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时统计支付交易信息
 * @Author: Mr.Lu
 * @Date: 2019/5/22 9:58
 */
@Component("autoStatistics")
@EnableScheduling
public class AutoStatistics {

    Logger logger = LoggerFactory.getLogger(AutoStatistics.class);


    /**
     * 支付统计 按天
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void statisticPay() {
        logger.info("开始统计交易信息-按天");
        //boolean flag = statisticsPayService.autoStatisticData();
        //logger.info("交易信息统计结果-按天：" + flag);
    }


    /**
     * 支付统计 按小时
     */
    @Scheduled(cron = "0 */59 * * * ?")
    public void statisticPayByHours() {
        logger.info("开始统计交易信息-按小时");
        //boolean flag = statisticsPayService.autoStatisticDataByHours();
        //logger.info("交易信息统计结果-按小时：" + flag);
    }


    /**
     * 代付统计 按天
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void statisticWithdrawal(){
        logger.info("开始统计代付交易信息-按天");
        //boolean flag = statisticsWithdrawalService.autoStatisticData();
       // logger.info("代付交易信息统计结果-按天：" + flag);
    }


    /**
     * 代付统计 按天
     */
    // public void statisticWithdrawalByHours(){
    //     logger.info("开始统计代付交易信息-按小时");
    //     boolean flag = statisticsWithdrawalService.autoStatisticDataByHours();
    //     logger.info("代付交易信息统计结果-按小时：" + flag);
    // }
}
