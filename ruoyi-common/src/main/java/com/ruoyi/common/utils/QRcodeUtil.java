package com.ruoyi.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 二维码生成工具类
 * @author xiezongjie
 * @version 1.0
 * @ClassName QRcodeUtil
 * @Date 2019-03-20 23:09
 */
public class QRcodeUtil {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 根据字符串生成对应的二维码图片
	 */
	public static BufferedImage createQRcode(String content){
		BufferedImage bufferedImage =null;
		try {
			BitMatrix byteMatrix = new MultiFormatWriter().encode(new String(content.getBytes(), "iso-8859-1"), BarcodeFormat.QR_CODE, 200, 200);
			bufferedImage = getBufferedImage(byteMatrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}

	private static BufferedImage getBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return bufferedImage;
	}
}
