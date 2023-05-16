package com.api.bussiness.channel.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * 通用http发送方法
 *
 * @author
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static String sendGet(String url, String param) {
        if(url==null)
            return null;
        String urlString = url;
        if(param !=null)
            urlString = url+ "?" + param;
        boolean isSSL = false;
        if (url.startsWith("https")) {
            isSSL = true;
            log.info("get_ssl_url:{}", urlString);
        } else if (url.startsWith("http")) {
            log.info("get_url:{}", urlString);
        } else{
            log.info("get_url:{}", urlString);
            return null;
        }

        String result = null;
        BufferedInputStream in = null;
        ByteArrayOutputStream baout = null;
        try {
            URL get = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) get.openConnection();
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setConnectTimeout(15 * 1000);
            if (isSSL) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
                ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                ((HttpsURLConnection) connection).setHostnameVerifier(new TrustAnyHostnameVerifier());
            }
            connection.connect();

            in = new BufferedInputStream(connection.getInputStream());
            baout = new ByteArrayOutputStream();

            int len;
            byte[] arr = new byte[1024];
            while ((len = in.read(arr)) != -1) {
                baout.write(arr, 0, len);
                baout.flush();
            }
            baout.close();

            result = baout.toString("utf-8");

            log.info("get_rsp:{}", result);

            connection.disconnect();
        } catch (Exception e) {
            log.error("get_exc:{}", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (baout != null) {
                    baout.close();
                }
            } catch (Exception ex) {
                log.error("get_exc:{}", ex);
            }
        }
        return result;
    }

    public static String sendPost(String url, String content) {
        boolean isSSL = false;
        if (url.startsWith("https")) {
            isSSL = true;
            log.info("post_ssl_url:{}", url);
        } else if (url.startsWith("http")) {
            log.info("post_url:{}", url);
        } else{
            return null;
        }
        log.info("post_req:{}", content);

        String result = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        ByteArrayOutputStream bout = null;
        try {
            URL post = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) post.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            if(isSSL){
                SSLContext sc = SSLContext.getInstance("SSL","SunJSSE");
                sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
                ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                ((HttpsURLConnection) connection).setHostnameVerifier(new TrustAnyHostnameVerifier());
            }
            connection.setConnectTimeout(15 * 1000);

            out = new BufferedOutputStream(connection.getOutputStream());

            if(content!=null){
                out.write(content.getBytes());
                out.flush();
            }

            in = new BufferedInputStream(connection.getInputStream());

            bout = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = in.read(arr)) != -1) {
                bout.write(arr, 0, len);
                bout.flush();
            }
            bout.close();

            result = bout.toString("utf-8");

            log.info("post_rsp:{}", result);

            connection.disconnect();
        } catch (Exception e) {
            log.error("post_exc:{}", e);
        } finally {
            try {
                if (bout != null) {
                    bout.close();
                }
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                log.error("post_exc:{}", ex);
            }
        }
        return result;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}