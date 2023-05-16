package com.api.bussiness;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

/**
 * @auther Lengff
 * @time 2020/9/6
 * @fileName TestController
 */
@RequestMapping
@RestController
public class TestController {
    public static void main(String[] args)throws AWTException {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = null;
        Toolkit tolkit = Toolkit.getDefaultToolkit();
        String[] lists ={"我只爱你四天,春天夏天秋天冬天","我只爱你三天,昨天,今天,明天.","我只爱你两天,白天,黑天","我只爱你一天,每一天","爱你么么哒"};
        Robot robot = new Robot();
        robot.delay(1000);//延迟1秒，主要是为了预留出打开窗口的时间，括号内的单位为毫秒
        tText = new StringSelection("官网数据刷新了,快去看看"); //自己定义就需要把这行注释，下行取消注释
        clip.setContents(tText, null);
        robot.keyPress( KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(3000);
        robot.keyPress( KeyEvent.VK_ENTER);
    }
}
