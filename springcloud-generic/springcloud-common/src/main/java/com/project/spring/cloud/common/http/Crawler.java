package com.project.spring.cloud.common.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * http请求处理
 */
public class Crawler {
    private static Logger logger = LoggerFactory.getLogger(Crawler.class);
    private static final OkHttpClient client;
    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    static {

        try {
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init((KeyStore) null);
//            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
//            }
//            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[]{trustManager}, null);
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            client = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("");
        }

    }

    /**
     * get请求
     *
     * @param url 请求地址
     * @return json字符串
     */
    public static String get(String url) {
        long cost, start = System.currentTimeMillis();
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                cost = System.currentTimeMillis() - start;
                logger.error("[Crawler] - failure, cost={}ms, url={}, response={}", cost, url, response);
                return null;
            }
            String content = response.body().string();
            cost = System.currentTimeMillis() - start;
            logger.debug("[Crawler] - succeed, cost={}ms, url={} ", cost, url);
            return content;
        } catch (IOException e) {
            cost = System.currentTimeMillis() - start;
            logger.error("[Crawler] - exception, cost={}ms, url={},\t occur exception", cost, url, e);
        }
        return null;
    }

    /**
     * 异步http请求
     *
     * @param url
     * @param callback
     */
    public static void asyncGet(String url, Callback callback) {
        long cost, start = System.currentTimeMillis();
        try {
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(callback);
            cost = System.currentTimeMillis() - start;
            logger.debug("[Crawler] - succeed, cost={}ms, url={} ", cost, url);
        } catch (Exception e) {
            cost = System.currentTimeMillis() - start;
            logger.error("[Crawler] - exception, cost={}ms, url={},\t occur exception", cost, url, e);
        }
    }

    /**
     * get请求，并序列化返回结果
     *
     * @param clazz 返回结果类对象
     * @param url   请求地址
     * @param <T>   返回结果类型
     * @return 返回结果对象
     */
    public static <T> T get(Class<T> clazz, String url) {
        String result = null;
        try {
            result = get(url);
            if (result != null) {
                return JSON.parseObject(result, clazz);
            }
        } catch (Exception e) {
            logger.error("[Crawler] - json deserialize exception, url={}, result={}", url, result, e);
        }
        return null;
    }

    /**
     * post请求
     *
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @return 返回结果对象
     */
    public static <T> String post(T params, String url) {
        return post(JSON.toJSONString(params), url);
    }

    /**
     * post请求
     *
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @return 返回结果对象
     */
    public static <T> void asyncPost(T params, String url, Callback callback) {
        long cost, start = System.currentTimeMillis();
        try {
            RequestBody body = RequestBody.create(JSON_TYPE, JSON.toJSONString(params));
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(callback);
            cost = System.currentTimeMillis() - start;
            logger.debug("[Crawler] - succeed, cost={}ms, url={} ", cost, url);
        } catch (Exception e) {
            cost = System.currentTimeMillis() - start;
            logger.error("[Crawler] - exception, cost={}ms, url={},\t occur exception", cost, url, e);
        }
    }

    /**
     * post请求
     *
     * @param clazz  返回结果类对象
     * @param params 请求参数
     * @param url    请求地址
     * @param <T>    请求参数类型
     * @param <R>    返回结果类型
     * @return 返回结果对象
     */
    public static <T, R> R post(Class<R> clazz, T params, String url) {
        String result = null;
        try {
            result = post(JSON.toJSONString(params), url);
            if (result != null) {
                return JSON.parseObject(result, clazz);
            }
        } catch (Exception e) {
            logger.error("[Crawler] - json deserialize exception, url={}, params={}, result={}", url, params, result, e);
        }
        return null;
    }

    /**
     * post请求
     *
     * @param json json格式的请求参数
     * @param url  请求地址
     * @return json字符串
     */
    public static String post(String json, String url) {
        long cost, start = System.currentTimeMillis();
        try {
            RequestBody body = RequestBody.create(JSON_TYPE, json);
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                cost = System.currentTimeMillis() - start;
                logger.error("[Crawler] - failure, cost={}ms, url={}, params={}", cost, url, json);
                return null;
            }
            String content = response.body().string();
            cost = System.currentTimeMillis() - start;
            logger.debug("[Crawler] - succeed, cost={}ms, url={}, params={}", cost, url, json);
            return content;
        } catch (IOException e) {
            cost = System.currentTimeMillis() - start;
            logger.error("[Crawler] - exception, cost={}ms, url={}, params={}", cost, url, json, e);
        }
        return null;
    }


}
