package com.dang.comicdownload.tool;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class HttpUtil {
    public static byte[] doGet(String url) {
        return HttpUtil.doGet(url,null);
    }
    public static byte[] doGet(String url, Map<String,String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        if(headers != null && headers.size()!= 0)
            setHeaders(request,headers);
        CloseableHttpResponse response = null;
        byte[] result = new byte[0];
        try {
            response = httpClient.execute(request);
            HttpEntity entry = response.getEntity();
            result = EntityUtils.toByteArray(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            HttpClientUtils.closeQuietly(httpClient);
            HttpClientUtils.closeQuietly(response);
        }
        return result;
    }

    private static HttpGet setHeaders(HttpGet request, Map<String,String> headers){
        for(Map.Entry<String,String> header: headers.entrySet()){
            request.setHeader(header.getKey(),header.getValue());
        }
        return request;
    }
    public static String doPost(){
        return null;
    }
}
