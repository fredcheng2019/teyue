package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 通知公告表 sys_telegram_notice
 *
 * @author fred
 */
public class SysTelegramNotice implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 机器人ID */
    private String botId;

    /** 机器人token */
    private String botToken;

    /** 公告内容 */
    private String noticeContent;

    public String getBotId() {
        return botId;
    }

    public void setBotId(String botId) {
        this.botId = botId;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }
}
