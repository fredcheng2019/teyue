package com.api.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 文件操作相关方法
 * 
 * @author mzw
 * 
 */
public class FileUtil {
	
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 以指定文件和分隔符读取文件的内容保存在list中，一行为一个list，然后再用list保存这些list
	 * 
	 * @param fileName
	 *            要读取的文件名
	 * @param spiltString
	 *            分隔符
	 * @return List<List<String>>
	 * @throws Exception
	 */
	public static List<List<String>> spTextReader(String fileName,
			String spiltString) {
		BufferedReader bufferedReader = null;
		List<List<String>> lines = new ArrayList<List<String>>();
		try {

			File file = new File(fileName);
			bufferedReader = new BufferedReader(new FileReader(file));
			String s;
			while ((s = bufferedReader.readLine()) != null) {
				List<String> line = new ArrayList<String>();
				String[] str = s.split(spiltString);
				for (String string : str) {
					line.add(string);
				}
				lines.add(line);
				// line.clear();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return lines;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param srcFileName
	 *            源文件路径
	 * @param dstFileName
	 *            目标文件路径
	 * @throws Exception
	 */
	public static void fileCopy(String srcFileName, String dstFileName) {
		File srcFile = new File(srcFileName);
		File dstFile = new File(dstFileName);
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			input = new FileInputStream(srcFile);
			output = new FileOutputStream(dstFile);
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}

	}

	public static boolean fileDelete(File file) {
		if (file.exists() && file.isFile()) {
			return file.delete();
		} else {
			return false;
		}
	}
	public static void main(String[] args) {
		fileCopy("E:\\j1.html.bak", "E:\\j1.html.bak");
	}

		/**
	 * 输入流转成字节数组
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] streamToBytes(InputStream in) throws IOException {
		byte[] buffer = new byte[5120];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int readSize;
		while ((readSize = in.read(buffer)) >= 0)
			out.write(buffer, 0, readSize);
		in.close();
		return out.toByteArray();
	}
	
	/**
	 * 返回状态信息
	 * @param code
	 * @param backMsg
	 * @return
	 */
	public static String getHeader(String code,String backMsg){
		StringBuffer msg = new StringBuffer();
		msg.append("<retcode>");
		msg.append(code);
		msg.append("</retcode>");
		msg.append("<retmsg>");
		msg.append(backMsg);
		msg.append("</retmsg>");
		return msg.toString();
	}
}
