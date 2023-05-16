package com.ruoyi.web.controller.system;


import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.merchant.domain.MpMerchant;
import com.ruoyi.merchant.domain.MpMerchantRSP;
import com.ruoyi.merchant.service.IMpMerchantService;
import com.ruoyi.system.domain.SysTelegramNotice;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * telegram机器人
 *
 * @author free
 */
@Controller
@RequestMapping("/system/telegram")
public class SysTelegramBotController extends BaseController {
    private String prefix = "system/telegram";
    @Autowired
    private IMpMerchantService mpMerchantService;

    private final String token = "5043907698:AAE_xfC77Eqz_FawBOW4fauebnhD35q2Q7U";

    @RequiresPermissions("system:telegramBot:view")
    @GetMapping()
    public String notice(ModelMap mmap) {
        mmap.put("token", token);
        mmap.put("chats", getChat(token));
        return prefix + "/notice";
    }
    /**
     * 查询列表
     */
    @RequiresPermissions("merchant:merchantInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MpMerchant mpMerchant) {
        //mpMerchant.setIsAgent(0);
        if (ShiroUtils.isAgent()) {
            // 如果是代理的话，查询的数据只能是该代理旗下的数据
            mpMerchant.setAgentId(ShiroUtils.getUserId());
        }
        startPage();
        List<MpMerchantRSP> list = mpMerchantService.selectMpMerchantList(mpMerchant);
        return getDataTable(list);
    }

    /**
     * 机器人发送公告
     */
    @RequiresPermissions("system:telegramBot:view")
    @Log(title = "机器人发送公告", businessType = BusinessType.OTHER)
    @PostMapping("/send")
    @ResponseBody
    public AjaxResult send(/*@RequestParam("image") MultipartFile file,*/HttpServletRequest request, SysTelegramNotice notice) {
       /* if (StringUtils.isEmpty(notice.getNoticeContent())) {
            return error("发送内容不能为空");
        }*/
        MultipartFile file = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart && StringUtils.isEmpty(notice.getNoticeContent())) {
            return error("发送内容不能为空");
        }
        //发送消息
        List<MpMerchantRSP> list = mpMerchantService.selectMpMerchantList(new MpMerchant());
        handleSendMsg(list, token, notice.getNoticeContent());
        return success();
    }



    private void handleSendMsg(List<MpMerchantRSP> list , String token, String message) {
        if(list!=null && list.size()>0) {
            for(MpMerchantRSP mpMerchant:list) {
                String[] str=StringUtils.isNotBlank(mpMerchant.getContactName()) ?
                        mpMerchant.getContactName().split(",") : null;
                if(str!=null && str.length>1) {
                    String chatId=str[1];
                    Map<String, Object> map = new HashMap<>();
                    map.put("chat_id", chatId);
                    map.put("disable_web_page_preview", false);
                    message = message.replace("#.", "\n");
                    map.put("text", message);
                    String url = "https://api.telegram.org/bot" + token + "/sendMessage";
                    new Thread(()->HttpsUtil.doPost(url, map)).start();
                }
            }
        }
    }

    private void handleSendPhoto(Set<String> set, String token, byte [] byteArr) {
        if (set == null || set.isEmpty()) {
            return;
        }
        for (String chatId : set) {
            TelegramBot bot = new TelegramBot(token);
            SendResponse response = bot.execute(new SendPhoto(chatId, byteArr));
            logger.info("telegram机器人发送图片 -> {}", response);
        }

    }


    /**
     * 获取群聊列表
     * @param token
     * @return
     */
    public Map<String, String> getChat(String token) {
        String result = HttpUtils.sendGet("https://api.telegram.org/bot" + token + "/getUpdates", null);
        logger.info("telegram机器人getUpdates结果：" + result);
        return getMap(result);
    }

    /**
     * 获取群聊列表
     * @param result
     * @return
     */
    public static Map<String, String> getMap(String result) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.fromObject(result);
            if (jsonObject.getBoolean("ok")) {
                JSONArray array = jsonObject.getJSONArray("result");

                for (int i = 0; i < array.size(); i++) {
                    JSONObject message = array.getJSONObject(i).optJSONObject("message");
                    if(message==null){
                        continue;
                    }
                    System.out.println(message);
                    JSONObject chat = message.optJSONObject("chat");
                    if(chat==null){
                        continue;
                    }
                    System.out.println(chat);

                    String id = chat.getString("id");
                    //map.put("key", id);
                    String title = chat.optString("title");
                    if(StringUtils.isEmpty(title)){
                        title = id;
                    }
                    map.put(id, title);
                }
            }
        }
        return map;
    }


   /* private Set<Map<String, String>> getDemoChats() {
        String result = "{\"ok\":true,\"result\":[{\"update_id\":513515063,\"message\":{\"message_id\":6,\"from\":{\"id\":1062411117,\"is_bot\":false,\"first_name\":\"jinbing\",\"last_name\":\"shen\",\"username\":\"shenjinbing\",\"language_code\":\"zh-hans\"},\"chat\":{\"id\":-422218279,\"title\":\"\\u9996\\u4fe1\\u6613=\\u6fb3\\u95e8168\\u62bc\\u91d1\\u4e24\\u4e07\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1612331292,\"new_chat_participant\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_member\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_members\":[{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"}]}},{\"update_id\":513515064,\"message\":{\"message_id\":7,\"from\":{\"id\":1062411117,\"is_bot\":false,\"first_name\":\"jinbing\",\"last_name\":\"shen\",\"username\":\"shenjinbing\",\"language_code\":\"zh-hans\"},\"chat\":{\"id\":-456738772,\"title\":\"\\u9996\\u4fe1\\u6613==dayingjia\\u5fc5\\u987b\\u7f51\\u94f6\\u4e0b\\u53d1\\u3010\\u62bc\\u91d12W\\u3011\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1612331550,\"new_chat_participant\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_member\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_members\":[{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"}]}},{\"update_id\":513515065,\"message\":{\"message_id\":8,\"from\":{\"id\":629346785,\"is_bot\":false,\"first_name\":\"A\",\"last_name\":\"M\\u5ba2\\u670d1\\ud83e\\udda9\\ud83e\\udda9\\ud83e\\udda9\"},\"chat\":{\"id\":629346785,\"first_name\":\"A\",\"last_name\":\"M\\u5ba2\\u670d1\\ud83e\\udda9\\ud83e\\udda9\\ud83e\\udda9\",\"type\":\"private\"},\"date\":1612331566,\"text\":\"/start\",\"entities\":[{\"offset\":0,\"length\":6,\"type\":\"bot_command\"}]}},{\"update_id\":513515066,\"message\":{\"message_id\":9,\"from\":{\"id\":1062411117,\"is_bot\":false,\"first_name\":\"jinbing\",\"last_name\":\"shen\",\"username\":\"shenjinbing\",\"language_code\":\"zh-hans\"},\"chat\":{\"id\":-308621077,\"title\":\"\\u9996\\u4fe1\\u5185\\u90e8\\u7fa4\",\"type\":\"group\",\"all_members_are_administrators\":false},\"date\":1612331640,\"left_chat_participant\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"left_chat_member\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"}}},{\"update_id\":513515067,\"message\":{\"message_id\":10,\"from\":{\"id\":1062411117,\"is_bot\":false,\"first_name\":\"jinbing\",\"last_name\":\"shen\",\"username\":\"shenjinbing\",\"language_code\":\"zh-hans\"},\"chat\":{\"id\":-308621077,\"title\":\"\\u9996\\u4fe1\\u5185\\u90e8\\u7fa4\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1612331656,\"new_chat_participant\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_member\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_members\":[{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"}]}},{\"update_id\":513515068,\"message\":{\"message_id\":18,\"from\":{\"id\":1403061117,\"is_bot\":false,\"first_name\":\"Canddy\",\"last_name\":\"Go\",\"username\":\"canddygo\",\"language_code\":\"zh-hans\"},\"chat\":{\"id\":1403061117,\"first_name\":\"Canddy\",\"last_name\":\"Go\",\"username\":\"canddygo\",\"type\":\"private\"},\"date\":1612361036,\"text\":\"/start\",\"entities\":[{\"offset\":0,\"length\":6,\"type\":\"bot_command\"}]}},{\"update_id\":513515069,\"message\":{\"message_id\":19,\"from\":{\"id\":1403061117,\"is_bot\":false,\"first_name\":\"Canddy\",\"last_name\":\"Go\",\"username\":\"canddygo\",\"language_code\":\"zh-hans\"},\"chat\":{\"id\":-404328029,\"title\":\"\\u4f1a\\u8bae\\u5ba4\",\"type\":\"group\",\"all_members_are_administrators\":true},\"date\":1612361166,\"new_chat_participant\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_member\":{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"},\"new_chat_members\":[{\"id\":1655836358,\"is_bot\":true,\"first_name\":\"\\u9996\\u4fe1\\u6613\\u5c0f\\u53ef\\u7231\",\"username\":\"CanddyCapBot\"}]}}]}";
        return getMap(result);
    }*/


    public static void main(String[] args) {
        String token="5043907698:AAE_xfC77Eqz_FawBOW4fauebnhD35q2Q7U";
        String token1="1655836358:AAETVvsxI8bIqeonbhC_Y3a2iUKZRzklT_U";
        String url="https://api.telegram.org/bot"+ token + "/sendMessage";
        String chatId="-725957727";
//        String result = HttpsUtil.doGet("https://api.telegram.org/bot" + token + "/getUpdates");
        StringBuffer html = new StringBuffer("当前通道情况\uD83D\uDCE3\uD83D\uDCE3\uD83D\uDCE3\n");
                html.append("\n");
                html.append("✅【111 云闪付转卡】 500-10000   \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\n");
                html.append("\n");
                html.append("✅【113 网银转卡】 500-5000   \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\n");
                html.append("\n");
                html.append("❌【666 网银小额】 100-500   \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\n");
                html.append("\n");
                html.append("✅【106 支付宝扫码】 2000-20000 \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\n");
                html.append("\n");
                html.append("✅【104  直冲微信小额话费】 50-100-200 \uD83D\uDD25\uD83D\uDD25\n");
                html.append("\n");
                html.append("✅【101  支付宝小额话费】 30-50-100-200 全层抗投！ (需要联系客服配置哦)\n");
                html.append("\n");
                html.append("✅【105 微信群红包】 200-5000  \uD83D\uDD25\uD83D\uDD25  开启  (费率有所调整 请咨询客服配置)\n");
                html.append("\n");
                html.append("✅【112 微信扫码】 1000-20000  \uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【112 微信扫码】  固额 58,88,188,288,388,588,688,888,988 全层抗投！ \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【119 淘宝群红包】 50-1000  \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【602 微信超级话费】 50、100、200  \uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【603 微信话费慢充】 50、100、200  \uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【613 支付宝话费慢充】 50、100、200  \uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("❌【616 微信Y币充值】  10-500 稳定60+ 暂停❌ \n");
                html.append("\n");
                html.append("✅【107支付宝口令红包\uD83E\uDDE7】 1000-10000   全层抗投！\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【668 QQ群红包】 200-5000  \uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【669  微信口令红包】 100、200、400、600、800、1000 \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append(" \n");
                html.append("✅【886 微信赞赏码】 固额:100、200  \uD83D\uDD25\uD83D\uDD25 开启\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("✅【888 支付宝小额扫码】  150-300  任意金额 全层抗投！ \uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\t\n");
                html.append("✅【115 USDT】200-10W,全层抗投！\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25\uD83D\uDD25 \n");
                html.append("\n");
                html.append("注：转卡通道仅限内层使用，请各位老板配合下，如排查出通道放置外层，系统检测到刷单者多，情况恶劣者，我司会考虑终止合作 ！！！\n" );
                html.append("请各商户放置内层哦，谢谢配合");
        new Thread(()->
        HttpUtils.sendGet(url, "chat_id=" + chatId + "&text=" +  URLEncoder.encode(html.toString()))).start();

        TelegramBot bot = new TelegramBot(token);
        bot.setUpdatesListener(updates -> {
            // ... process updates
            // return id of last processed update or confirm them all

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));
        System.out.println(response.isOk());
    }

}
