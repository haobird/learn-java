package com.jd.ecc.cloudbiz.common.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static int HTTPCLIENT_MAX_CONNECTION = 2000;
    private static int HTTPCLIENT_TIMEOUT_SOCKET = 15000;
    private static int HTTPCLIENT_TIMEOUT_CONNECT = 5000;
    private static int HTTPCLIENT_TIMEOUT_CONNECTION_REQUEST = 3000;

    private static SSLConnectionSocketFactory sslConnectionSocketFactory = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static RequestConfig defaultRequestConfig = null;

    static {
        InputStream appIn = HttpClientUtils.class.getClassLoader().getResourceAsStream("httpClient.properties");
        Properties appProps = new Properties();
        try {
            appProps.load(appIn);
            HTTPCLIENT_MAX_CONNECTION = Integer.valueOf(appProps.getProperty("httpClient.max.connection"));
            HTTPCLIENT_TIMEOUT_SOCKET = Integer.valueOf(appProps.getProperty("httpClient.timeout.socket"));
            HTTPCLIENT_TIMEOUT_CONNECT = Integer.valueOf(appProps.getProperty("httpClient.timeout.connect"));
            HTTPCLIENT_TIMEOUT_CONNECTION_REQUEST = Integer.valueOf(appProps.getProperty("httpClient.timeout.connection.request"));
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                appIn.close();
            } catch (IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null,  new TrustSelfSignedStrategy());
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();

            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(HTTPCLIENT_MAX_CONNECTION);//max connection
            cm.setDefaultMaxPerRoute(cm.getMaxTotal());

            defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(HTTPCLIENT_TIMEOUT_SOCKET)
                    .setConnectTimeout(HTTPCLIENT_TIMEOUT_CONNECT)
                    .setConnectionRequestTimeout(HTTPCLIENT_TIMEOUT_CONNECTION_REQUEST)
                    .build();
        } catch (Exception e) {
            logger.error("初始化HttpClientPool出现异常！", e);
        }
    }

    /**
     * 得到client
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(cm)
                .build();
        return httpClient;
    }

    /**
     * 关闭连接对象
     *
     * @param response
     */
    public static void closeResponse(CloseableHttpResponse response) {
        if (response != null) {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                //此处异常打印也没什么作用
            }
            try {
                response.close();
            } catch (IOException e) {
                //此处异常打印也没什么作用
            }
        }
    }
}