package com.by_syk.schttableserver.util.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.by_syk.schttableserver.util.TokenCookie;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 高效网络请求工具类
 * （封装OkHttp）
 * 
 * @author shijkui
 */
public class HttpUtil {
    private static OkHttpClient okHttpClient = null;
    private static OkHttpClient okHttpClient2 = null;
    
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static {
        // 超时设置
        // OkHttp2
//        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
//        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
//        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        // OkHttp3
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        okHttpClient2 = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }
    
    private static Request createGetRequest(String url, Map<String, String> headerMap) {
        Request.Builder requestBuilder = (new Request.Builder())
                .url(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        
        return requestBuilder.build();
    }
    
    private static Request createPostRequest(String url, String json, Map<String, String> headerMap) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        
        Request.Builder requestBuilder = (new Request.Builder())
                .url(url)
                .post(requestBody);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        
        return requestBuilder.build();
    }
    
    private static Request createPostRequestGBK(String url, String paras, Map<String, String> headerMap) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=gbk"), paras);
        
        Request.Builder requestBuilder = (new Request.Builder())
                .url(url)
                .post(requestBody);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        
        return requestBuilder.build();
    }
    
    private static Request createPostRequest(String url, Map<String, String> paraMap, Map<String, String> headerMap) {
        // OkHttp2
        // 默认编码UTF-8
//        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
//        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
//            formEncodingBuilder.add(entry.getKey(), entry.getValue());
//        }
//        RequestBody requestBody = formEncodingBuilder.build();
        // OkHttp3
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = formBodyBuilder.build();
        
        Request.Builder requestBuilder = (new Request.Builder())
                .url(url)
                .post(requestBody);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        
        return requestBuilder.build();
    }
    
    /**
     * GET请求
     * 
     * @param url 链接
     * @param hearderMap 头部信息
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headerMap) throws IOException {
        Request request = createGetRequest(url, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        //return response.body().string();
        if (response.isSuccessful()) {
            // body.string() which closes body anyway, if there's or there's no exception
            // https://github.com/square/okhttp/issues/2311
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * GET请求
     * 
     * @param url
     * @param cookie
     * @return
     * @throws IOException
     */
    public static String get(String url, String cookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        
        return get(url, headerMap);
    }
    
    /**
     * GET请求
     * 
     * @param url
     * @param cookie
     * @return
     */
    public static String getNoException(String url, String cookie) {
        try {
            return get(url, cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 异步GET请求
     * 
     * @param url
     * @param cookie
     * @param callback 回调接口
     */
    public static void get(String url, String cookie, Callback callback) {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        Request request = createGetRequest(url, headerMap);
        
        okHttpClient.newCall(request).enqueue(callback);
    }
    
    /**
     * GET请求
     * 
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, (String) null);
    }
    
    /**
     * GET请求
     * 
     * @param url
     * @return
     */
    public static String getNoException(String url) {
        return getNoException(url, (String) null);
    }
    
    /**
     * 异步GET请求
     * 
     * @param url
     * @param callback 回调接口
     */
    public static void get(String url, Callback callback) {
        get(url, (String) null, callback);
    }
    
    /**
     * 获取Cookie
     * 
     * @param url
     * @param cookie
     * @return
     * @throws IOException
     */
    public static String getReturnCookie(String url, String cookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        Request request = createGetRequest(url, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            List<String> cookieList = response.headers("Set-Cookie");
            String cookies = "";
            for (String co : cookieList) {
                cookies += co + "; ";
            } 
            if (!cookies.isEmpty()) {
                cookies = cookies.substring(0, cookies.length() - 2);
                cookies = cookies.replaceAll(";*\\s*(p|P)ath=[^;]*", "");
            }
            response.body().close();
            return cookies;
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 获取Cookie
     * 
     * @param url
     * @return
     * @throws IOException
     */
    public static String getReturnCookie(String url) throws IOException {
        return getReturnCookie(url, (String) null);
    }
    
    /**
     * 获取Cookie
     * 
     * @param url
     * @param tokenCookie
     * @return
     * @throws IOException
     */
    public static String getReturnCookie(String url, TokenCookie tokenCookie) throws IOException {
        Request request = createGetRequest(url, null);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            List<String> cookieList = response.headers("Set-Cookie");
            String cookies = "";
            for (String cookie : cookieList) {
                cookies += cookie + "; ";
            }
            if (!cookies.isEmpty()) {
                cookies = cookies.substring(0, cookies.length() - 2);
                cookies = cookies.replaceAll(";*\\s*(p|P)ath=[^;]*", "");
            }
            tokenCookie.set(cookies);
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 获取Cookie
     * 
     * @param url
     * @param headerMap
     * @param tokenCookie
     * @return
     * @throws IOException
     */
    public static String getReturnCookie(String url, Map<String, String> headerMap, TokenCookie tokenCookie) throws IOException {
        Request request = createGetRequest(url, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            List<String> cookieList = response.headers("Set-Cookie");
            String cookies = "";
            for (String cookie : cookieList) {
                cookies += cookie + "; ";
            }
            if (!cookies.isEmpty()) {
                cookies = cookies.substring(0, cookies.length() - 2);
                cookies = cookies.replaceAll(";*\\s*(p|P)ath=[^;]*", "");
            }
            tokenCookie.set(cookies);
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * POST请求
     * 
     * @param url 链接
     * @param json JSON参数
     * @param headerMap 头部信息
     * @return
     * @throws IOException
     */
    public static String post(String url, String json, Map<String, String> headerMap) throws IOException {
        Request request = createPostRequest(url, json, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param json
     * @param cookie
     * @return
     * @throws IOException
     */
    public static String post(String url, String json, String cookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        
        return post(url, json, headerMap);
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param json
     * @param cookie
     * @return
     */
    public static String postNoException(String url, String json, String cookie) {
        try {
            return post(url, json, cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 异步POST请求
     * 
     * @param url
     * @param json
     * @param cookie
     * @param callback 回调接口
     * @throws IOException
     */
    public static void post(String url, String json, String cookie, Callback callback) {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        Request request = createPostRequest(url, json, headerMap);
        
        okHttpClient.newCall(request).enqueue(callback);
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String post(String url, String json) throws IOException {
        return post(url, json, (String) null);
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param json
     * @return
     */
    public static String postNoException(String url, String json) {
        return postNoException(url, json, (String) null);
    }
    
    /**
     * 异步POST请求
     * 
     * @param url
     * @param json
     * @param callback 回调接口
     * @throws IOException
     */
    public static void post(String url, String json, Callback callback) {
        post(url, json, (String) null, callback);
    }
    
    /**
     * POST请求（GBK编码）
     * 
     * @param url 链接
     * @param paras 参数
     * @param headerMap 头部信息
     * @return
     * @throws IOException
     */
    public static String postGBK(String url, String paras, Map<String, String> headerMap) throws IOException {
        Request request = createPostRequestGBK(url, paras, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * POST请求（GBK编码）
     * 
     * @param url
     * @param paras
     * @param cookie
     * @return
     * @throws IOException
     */
    public static String postGBK(String url, String paras, String cookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        
        return postGBK(url, paras, headerMap);
    }
    
    /**
     * POST请求
     * 
     * @param url 链接
     * @param paraMap 参数
     * @param headerMap 头部信息
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> paraMap, Map<String, String> headerMap) throws IOException {
        Request request = createPostRequest(url, paraMap, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param paraMap
     * @param cookie
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> paraMap, String cookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        
        return post(url, paraMap, headerMap);
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param paraMap
     * @param headerMap
     * @return
     */
    public static String postNoException(String url, Map<String, String> paraMap, Map<String, String> headerMap) {
        try {
            return post(url, paraMap, headerMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param paraMap
     * @param cookie
     * @return
     */
    public static String postNoException(String url, Map<String, String> paraMap, String cookie) {
        try {
            return post(url, paraMap, cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 异步POST请求
     * 
     * @param url
     * @param paraMap
     * @param headerMap
     * @param callback 回调接口
     * @throws IOException
     */
    public static void post(String url, Map<String, String> paraMap, Map<String, String> headerMap, Callback callback) {
        Request request = createPostRequest(url, paraMap, headerMap);
        
        okHttpClient.newCall(request).enqueue(callback);
    }
    
    /**
     * 异步POST请求
     * 
     * @param url
     * @param paraMap
     * @param cookie
     * @param callback 回调接口
     * @throws IOException
     */
    public static void post(String url, Map<String, String> paraMap, String cookie, Callback callback) {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        Request request = createPostRequest(url, paraMap, headerMap);
        
        okHttpClient.newCall(request).enqueue(callback);
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param paraMap
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> paraMap) throws IOException {
        return post(url, paraMap, (Map<String, String>) null);
    }
    
    /**
     * POST请求
     * 
     * @param url
     * @param paraMap
     * @return
     */
    public static String postNoException(String url, Map<String, String> paraMap) {
        return postNoException(url, paraMap, (Map<String, String>) null);
    }
    
    /**
     * 异步POST请求
     * 
     * @param url
     * @param paraMap
     * @param callback 回调接口
     * @throws IOException
     */
    public static void post(String url, Map<String, String> paraMap, Callback callback) {
        post(url, paraMap, (Map<String, String>) null, callback);
    }
    
    /**
     * 获取Cookie
     * 
     * @param url
     * @param paraMap
     * @return
     * @throws IOException
     */
    public static String postReturnCookie(String url, Map<String, String> paraMap) throws IOException {
        Request request = createPostRequest(url, paraMap, null);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            List<String> cookieList = response.headers("Set-Cookie");
            String cookies = "";
            for (String cookie : cookieList) {
                cookies += cookie + "; ";
            }
            if (!cookies.isEmpty()) {
                cookies = cookies.substring(0, cookies.length() - 2);
                cookies = cookies.replaceAll(";*\\s*(p|P)ath=[^;]*", "");
            }
            response.body().close();
            return cookies;
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 获取 Cookie
     * 
     * @param url
     * @param paraMap
     * @param cookie
     * @param tokenCookie
     * @return
     * @throws IOException
     */
    public static String postReturnCookie(String url, Map<String, String> paraMap, String cookie, TokenCookie tokenCookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        Request request = createPostRequest(url, paraMap, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            List<String> cookieList = response.headers("Set-Cookie");
            String cookies = "";
            for (String co : cookieList) {
                cookies += co + "; ";
            }
            if (!cookies.isEmpty()) {
                cookies = cookies.substring(0, cookies.length() - 2);
                cookies = cookies.replaceAll(";*\\s*(p|P)ath=[^;]*", "");
            }
            tokenCookie.set(cookies);
            return response.body().string();
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 获取 Cookie
     * 
     * @param url
     * @param paraMap
     * @param tokenCookie
     * @return
     * @throws IOException
     */
    public static String postReturnCookie(String url, Map<String, String> paraMap, TokenCookie tokenCookie) throws IOException {
        return postReturnCookie(url, paraMap, null, tokenCookie);
    }
    
    /**
     * 获取 Location
     * 
     * @param url
     * @param paraMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String postReturnLocation(String url, Map<String, String> paraMap, Map<String, String> headerMap) throws IOException {
        Request request = createPostRequest(url, paraMap, headerMap);
        
        Response response = okHttpClient2.newCall(request).execute();
        if (response.isRedirect()) {
            String location = response.header("Location");
            response.body().close();
            return location;
        } else {
            response.body().close();
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 获取 Location
     * 
     * @param url
     * @param paraMap
     * @param cookie
     * @return
     * @throws IOException
     */
    public static String postReturnLocation(String url, Map<String, String> paraMap, String cookie) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        
        return postReturnLocation(url, paraMap, headerMap);
    }
    
//    public static String formatParams(List<BasicNameValuePair> params) {
//        return URLEncodedUtils.format(params, CHARSET_NAME);
//    }
    
    /**
     * 下载文件
     * 
     * @param url
     * @param headerMap
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static File downloadFile(String url, Map<String, String> headerMap, String targetFile) throws IOException {
        Request request = createGetRequest(url, headerMap);
        
        Response response = okHttpClient.newCall(request).execute();
        
        ResponseBody responseBody = response.body();
        InputStream inputStream = responseBody.byteStream();
        FileOutputStream fos = new FileOutputStream(targetFile);
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        inputStream.close();
        responseBody.close();
        
        return new File(targetFile);
    }
    
    /**
     * 下载文件
     * 
     * @param url
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static File downloadFile(String url, String cookie, String targetFile) throws IOException {
        Map<String, String> headerMap = null;
        if (cookie != null) {
            headerMap = new HashMap<>();
            headerMap.put("Cookie", cookie);
        }
        
        return downloadFile(url, headerMap, targetFile);
    }
    
    public static File downloadFile(String url, String targetFile) throws IOException {
        return downloadFile(url, (Map<String, String>) null, targetFile);
    }
    
    public static File downloadFileNoException(String url, String cookie, String targetFile) {
        try {
            return downloadFile(url, cookie, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static File downloadFileNoException(String url, String targetFile) {
        return downloadFileNoException(url, null, targetFile);
    }
}
