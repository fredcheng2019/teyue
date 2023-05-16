package com.ruoyi.web.controller.tool;

import com.ruoyi.common.utils.GoogleCodeUtil;
import com.ruoyi.common.utils.QRcodeUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * 谷歌身份验证器
 */
@Controller
@RequestMapping("/tool/googleCode")
public class GoogleCodeController {

    // 生成二维码图片
    @RequestMapping("createQRcode")
    public ResponseEntity<byte[]> createQRcode(String userId, String secret) throws Exception {

        String otpauth = GoogleCodeUtil.createOtpauth(userId, secret);
        BufferedImage bufferedImage = QRcodeUtil.createQRcode(otpauth);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", out);
        byte[] imageContent = out.toByteArray();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    // 校验谷歌动态密钥
    @RequestMapping("validCode")
    @ResponseBody
    public String validCode(String code,String secret){
        boolean result = GoogleCodeUtil.ValidCode(code, secret);
        if (result){
            return "0";
        }
        return "1";
    }
}
