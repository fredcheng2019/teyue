package com.api.bussiness.merchant.service;

public interface TelegramNotifyService {

	void sendMsgToMerchantChatGroup(String token,String chatId,String msg);

	void sendMsgToChannelChatGroup(String token,String chatId,String msg);
}
