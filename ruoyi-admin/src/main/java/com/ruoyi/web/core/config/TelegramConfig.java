package com.ruoyi.web.core.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther Lengff
 * @time 2022/1/9
 * @fileName TelegramConfig
 */
//@Configuration
public class TelegramConfig {
    /**
     * token
     */
    @Value("${telegram-bot.token}")
    private String telegramBotToken;

   // @Bean
    public TelegramBot telegramBot() {
        System.out.println("-----------------------------------------注入");
        // Create your bot passing the token received from @BotFather
        TelegramBot telegramBot = new TelegramBot(telegramBotToken);
        return telegramBot;
    }
}
