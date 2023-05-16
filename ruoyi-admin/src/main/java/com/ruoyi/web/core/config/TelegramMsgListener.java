package com.ruoyi.web.core.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther Lengff
 * @time 2022/1/9
 * @fileName TelegramMsgListener
 */
@Service
public class TelegramMsgListener implements UpdatesListener {

    private Logger log = LoggerFactory.getLogger(TelegramMsgListener.class);


    /**
     * bot
     */
    private TelegramBot bot;


    public TelegramMsgListener() {
    }

    public TelegramMsgListener(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * 根据消息格式分发：操作 + 消息内容
     * 查单消息格式:查单 单号或者图片+单号
     * 催单消息格式:催单 单号
     * 下发消息格式:下发 姓名+银行卡/支付宝+金额
     * 查余额消息格式:查余额 全部/指定通道名称
     * @param list
     * @return
     */
    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            try {
                log.info("机器人收到消息 -> {}", update);
                System.out.println(update.message().text());
//                sendMessage((byte) 2, update.message().chat().id(), "收到您的消息：" + update.message().text());
                route(update);
            } catch (Exception e) {
                log.warn("出现异常");
                log.warn("{}",e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void route(Update update){
        String message = update.message().text();
        if(message.contains("查单")){
            //查单 202201220902200001
            String code = message.split(" ")[1];
        }
        else if(message.contains("催单")){

        }
        else if(message.contains("下发")){
            //todo
        }
        else if(message.contains("查余额")){
            //todo
        }else{
            //默认查单：图片+单号

        }

    }
    /**
     * 发送消息
     *
     * @param type   消息类型
     * @param chatId 会话ID
     * @param text   消息内容
     */
    public void sendMessage(Byte type, long chatId, String text) {
        SendResponse response;
        if (type == 1) {
            // 图片
            response = bot.execute(new SendPhoto(chatId, text));
        } else {
            // 文本
            response = bot.execute(new SendMessage(chatId, text));
        }
        log.info("发送消息 -> {}", response);
    }

    public void close() {
        this.bot.removeGetUpdatesListener();
    }
}
