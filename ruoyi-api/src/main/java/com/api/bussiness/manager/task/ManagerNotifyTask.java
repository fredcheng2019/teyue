package com.api.bussiness.manager.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.bussiness.dao.order.PayDao;
import com.api.bussiness.dao.table.order.MpPay;
import com.api.bussiness.merchant.service.MerchantNotifyService;

@Component
public class ManagerNotifyTask {
	
	private static int MCH_PAY_NOTIFY_TASK_STATUS = 0;//0为执行、1执行中
	
	private static Logger log = LoggerFactory.getLogger(ManagerNotifyTask.class);
	
	@Autowired
	PayDao payDao;
	
	@Autowired
	MerchantNotifyService merchantNotifyService;
	
	@Scheduled(cron = "0/20 * * * * ? ")//每隔20s执行一次
    public void run(){
		try {
			log.info("通知商户支付结果:{}", ManagerNotifyTask.MCH_PAY_NOTIFY_TASK_STATUS);
			//判断上次任务是否执行完
			if(ManagerNotifyTask.MCH_PAY_NOTIFY_TASK_STATUS == 0) {
				ManagerNotifyTask.MCH_PAY_NOTIFY_TASK_STATUS = 1;
				//查询需要通知的订单
				List<MpPay> mpPays = payDao.getWaitToNotifyPay();
				log.info("待通知数量:{}条",mpPays.size());
				if(mpPays.size()>0) {
					for(int i=0;i<mpPays.size();i++) {
						try {
							merchantNotifyService.payNotify(mpPays.get(i));
						}catch (Exception e) {
							log.error("通知 过程异常:{}",e.getMessage());
						}
					}
				}
				ManagerNotifyTask.MCH_PAY_NOTIFY_TASK_STATUS = 0;
			}else {
				log.info("定时通知-上次通知没有执行完成");
				return;
			}
		}catch (Exception e) {
			ManagerNotifyTask.MCH_PAY_NOTIFY_TASK_STATUS = 0;
			log.error("支付通知异常:{}",e.getMessage());
		}
	}

}
