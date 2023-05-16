package com.api.bussiness.merchant.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.api.bussiness.dao.wallet.MerchantWalletDao;


@Component
@Transactional(rollbackFor = Exception.class)
public class CapitalFlowTask {
	
	private static Logger log = LoggerFactory.getLogger(CapitalFlowTask.class);
	
	@Autowired
	MerchantWalletDao merchantWalletDao;
	
	/**
	 * 待结算1、2更新
	 */
	@Scheduled(cron = "0 0 11 * * ? ")//每天11点执行
    public void run(){
		log.info("资金流处理开始");
		
		int result = merchantWalletDao.updateUnliquidatedBalance();
		
		log.info("资金流处理结束,更新条数:{}",result);
	}

}
