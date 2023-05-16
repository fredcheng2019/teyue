package com.ruoyi.web.core.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @auther Lengff
 * @time 2022/1/10
 * @fileName ProcessConfig
 */
//@Component
public class TelegramApplicationRunner implements ApplicationRunner {

    //@Autowired
    private TelegramBot telegramBot;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        telegramBot.setUpdatesListener(new TelegramMsgListener(telegramBot));
    }
}
