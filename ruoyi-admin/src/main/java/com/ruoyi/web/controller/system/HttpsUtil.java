package com.ruoyi.web.controller.system;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 */
public class HttpsUtil {


    /**
     * get请求 3000毫秒超时
     *
     * @param url 请求路径
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, 3000);
    }

    /**
     * get请求
     *
     * @param url
     * @param timeout 超时时间（毫秒）
     * @return
     */
    public static String doGet(String url, int timeout) {
        try {
            CloseableHttpClient client = null;

            /* 相信自己的CA和所有自签名的证书 */
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

            /* 不做证书校验 */
            sslcontext.init(null, new TrustManager[]{truseAllManager}, null);

            /* 只允许使用TLSv1协议 */
            // SSLConnectionSocketFactory sslsf = new
            // SSLConnectionSocketFactory(
            // sslcontext, protocols, null,
            // SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // SSLConnectionSocketFactory sslsf = new
            // SSLConnectionSocketFactory(sslcontext);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                    //new String[]{"TLSv1.2"},
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            // return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify("simp.com", session);
                            return true;
                        }
                    });//sslEnabledProtocols="SSLv2Hello,TLSv1,TLSv1.1,TLSv1.2,SSL"
            client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            //发送get请求
            HttpGet request = new HttpGet(url);
            request.setHeader("Content-Type", "application/json;charset=UTF-8");
            //request.setHeader("X-Auth-User", user);
            //request.setHeader("X-Auth-Code", code);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout).build();
            request.setConfig(requestConfig);
            // System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
            HttpResponse response = client.execute(request);
            /**读取服务器返回过来的json字符串数据**/
            String strResult = EntityUtils.toString(response.getEntity());
            return strResult;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("请求异常：" + e.getMessage() + url);
        }

        return null;
    }

    /**
     * post请求(用于key-value格式的参数)
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map params) {

        BufferedReader in = null;
        try {
            // 定义HttpClient
            // HttpClient client = new DefaultHttpClient();
            // CloseableHttpClient client = HttpClients.createDefault();

            CloseableHttpClient httpclient = null;

            /* 相信自己的CA和所有自签名的证书 */
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

            /* 不做证书校验 */
            sslcontext.init(null, new TrustManager[]{truseAllManager}, null);

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    //new String[]{"TLSv1.2"},
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            // return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify("simp.com", session);
                            return true;
                        }
                    });

            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            //httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);//超时时间毫秒
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            //request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setURI(new URI(url));

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(15000)
                    .setSocketTimeout(15000).build();
            request.setConfig(requestConfig);

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
                //System.out.println(name +"-"+value);
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {    //请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(), "utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                return sb.toString();
            } else {    //
                System.out.println("状态码：" + code + url + params);
                return null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("请求异常：" + e.getMessage() + url + params);
            return null;
        }
    }

    /**
     * post请求(用于key-value格式的参数)
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPostForm(String url, Map params) {

        BufferedReader in = null;
        try {
            // 定义HttpClient
            // HttpClient client = new DefaultHttpClient();
            // CloseableHttpClient client = HttpClients.createDefault();

            CloseableHttpClient httpclient = null;

            /* 相信自己的CA和所有自签名的证书 */
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

            /* 不做证书校验 */
            sslcontext.init(null, new TrustManager[]{truseAllManager}, null);

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    //new String[]{"TLSv1.2"},
                    new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            // return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify("simp.com", session);
                            return true;
                        }
                    });

            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            //httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);//超时时间毫秒
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            //request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setURI(new URI(url));
            request.setHeader("Content-Type", "multipart/form-data;charset=UTF-8");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(15000)
                    .setSocketTimeout(15000).build();
            request.setConfig(requestConfig);

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
                //System.out.println(name +"-"+value);
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

            HttpResponse response = httpclient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {    //请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(), "utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }

                in.close();

                return sb.toString();
            } else {    //
                System.out.println("状态码：" + code + url + params);
                return null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("请求异常：" + e.getMessage() + url + params);
            return null;
        }
    }

    /**
     * post请求（用于请求json格式的参数）
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String params) throws Exception {

        // CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = null;
        /* 相信自己的CA和所有自签名的证书 */
        SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        /* 不做证书校验 */
        sslcontext.init(null, new TrustManager[]{truseAllManager}, null);
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                //new String[]{"TLSv1.2"},
                new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"},
                null,
                new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        // return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify("simp.com", session);
                        return true;
                    }
                });

        httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        //	httpPost.setHeader("X-Auth-User", user);
        //	httpPost.setHeader("X-Auth-Code", code);
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String jsonString = EntityUtils.toString(responseEntity);
            return jsonString;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("请求异常：" + e.getMessage() + url + params);
            }
        }
    }

    /**
     * 重写验证方法，取消检测ssl
     */
    private static TrustManager truseAllManager = new X509TrustManager() {

        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {

        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    };

}
