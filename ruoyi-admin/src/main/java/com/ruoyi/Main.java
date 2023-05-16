package com.ruoyi;

import com.ruoyi.common.utils.http.HttpUtils;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //https://api.telegram.org/bot1655836358:AAETVvsxI8bIqeonbhC_Y3a2iUKZRzklT_U/sendMessage?chat_id=xxxxxxx&text=Hi+John
        //https://api.telegram.org/bot1655836358:AAETVvsxI8bIqeonbhC_Y3a2iUKZRzklT_U/getUpdates
       /* try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new ChannelHandlers());
            telegramBotsApi.registerBot(new DirectionsHandlers());
            telegramBotsApi.registerBot(new RaeHandlers());
            telegramBotsApi.registerBot(new WeatherHandlers());
            telegramBotsApi.registerBot(new TransifexHandlers());
            telegramBotsApi.registerBot(new FilesHandlers());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/
        //String result = HttpUtils.sendGet("https://api.telegram.org/bot1655836358:AAETVvsxI8bIqeonbhC_Y3a2iUKZRzklT_U/getUpdates", null);
       // System.out.println(result);
        //-422218279   -456738772  -308621077   -308621077
        String text="大家好，我是首信易小可爱";
       // List<String> chatIds = Arrays.asList("-422218279","-456738772","-308621077");
        List<String> chatIds = Arrays.asList("629346785");
        for (String chatId : chatIds){
            String result = HttpUtils.sendGet("https://api.telegram.org/bot1655836358:AAETVvsxI8bIqeonbhC_Y3a2iUKZRzklT_U/sendMessage",
                    "chat_id="+chatId+"&text="+text);
            System.out.println(result);
        }
    }
}
