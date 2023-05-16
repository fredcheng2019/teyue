package com.sifang.test.channel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import com.api.base.util.MD5;

//import com.sifang.base.util.MD5;

public class WanMeiFuTest {
	
	    //public static final String key = "2b8b43d1a0a24172a0d16278d8307f0b";105031616015正式
		public static final String key = "b92d73dc0587c4d7bf8d639f76fc4cecb3c6820d";
		

		//交易接口
		public static final String POST_URL = "http://wmpay.wshangfu.com/apisubmit";
		public static String post_data = "version=1.0&customerid=10000116&sdorderno=201905191034120005&total_fee=2000&paytype=bank_qrcode&notifyurl=123&returnurl=123&remark=sifang&sign=";
		public static String sign_data = "version=1.0&customerid=10000116&total_fee=2000&sdorderno=2019051910341200061&notifyurl=123&returnurl=123&b92d73dc0587c4d7bf8d639f76fc4cecb3c6820d";
		
		

		public static void readContentFromPost() throws Exception {
			long start = System.currentTimeMillis();
			// Post请求的url，与get不同的是不需要带参数
			URL postUrl = new URL(POST_URL);
			// 打开连接
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			//connection.setRequestProperty("Content-Type", "application/text");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			
			String sign = MD5.MD5Encode(sign_data).toLowerCase();
			System.out.println("得到的sign:"+sign);
			String data = post_data+sign;
			System.out.println("请求报文:"+data);
	        out.write(data.getBytes());
			out.flush();
			out.close(); // flush and close
			connection.getInputStream();
			long end = System.currentTimeMillis();
			System.out.println("运行时间：" + (end - start) + "毫秒");// 应该是end - start
			// 获取响应头信息
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();
			System.out.println("结果：" + bos.toString("utf-8"));
			connection.disconnect();
		}

		/** */
		/**
		 * @param args
		 * @throws Exception
		 */
		public static void main(String[] args) throws Exception {
			
			readContentFromPost();
			
		}

		/**
	     * 向指定URL发送GET方法的请求
	     * 
	     * @param url
	     *            发送请求的URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return URL 所代表远程资源的响应结果
	     */
	    public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
	 
	    /**
	     * 向指定 URL 发送POST方法的请求
	     * 
	     * @param url
	     *            发送请求的 URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return 所代表远程资源的响应结果
	     */
	    public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    

}
