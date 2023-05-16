package com.api.bussiness.merchant.service.impl;

import com.api.bussiness.dao.order.MerchantDao;
import com.api.bussiness.merchant.service.TelegramNotifyService;
import com.api.bussiness.channel.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("TelegramNotifyService")
public class TelegramNotifyServiceImpl implements TelegramNotifyService {
	
	private static Logger log = LoggerFactory.getLogger(TelegramNotifyService.class);
	
	@Autowired
    MerchantDao merchantDao;


	@Override
	public void sendMsgToMerchantChatGroup(String token, String chatId, String msg) {
		sendMsg(token,chatId,msg);
	}

	@Override
	public void sendMsgToChannelChatGroup(String token, String chatId, String msg) {
		sendMsg(token,chatId,msg);
	}

	private void sendMsg(String token, String chatId, String msg){
		String teleUrl = "https://api.telegram.org/bot" + token + "/sendMessage";
		new Thread(()->HttpUtils.sendGet(teleUrl, "chat_id=" + chatId + "&text=" + msg)).start();
	}
}
