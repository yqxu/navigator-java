package com.pingpongx.smb.warning.biz.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpAPI {
    private static final Integer CONNECT_TIMEOUT = 120000;
    private static final Integer CONNECTION_REQUEST_TIMEOUT = 120000;
    private static final Integer SOCKET_TIMEOUT = 120000;
    private static final Integer SO_TIMEOUT = 120000;
    private static final Integer MAX_CONN_TOTAL = 200;
    private static final Integer MAX_CONN_PER_ROUTE = 100;
    private static final Integer SUCCESS = 200;
    private static final String CHARSET = "UTF-8";
    private CloseableHttpClient httpClient = null;
    private String responseBody;
    private String responseHeader;
    private long duration;
    private Header[] headers;
    private Map<String, String> remainHead = new HashMap<>();

    public HttpAPI() {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .setSoTimeout(SO_TIMEOUT)
                .build();

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setValidateAfterInactivity(1000);
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .setConnectionManager(connManager)
                .build();
    }

    public HttpResult doGet(String url, Map<String, Object> headerMap, Map<String, Object> params) throws IOException, URISyntaxException {
        long time = System.currentTimeMillis();
        URIBuilder uriBuilder = new URIBuilder(url);

        if (params != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        // 调用不带参数的get请求
        HttpResult httpResult =doGet(uriBuilder.build().toString(), headerMap);
        duration = System.currentTimeMillis() - time;
        return httpResult;
    }

    public HttpResult doGet(String url, Map<String, Object> headerMap) throws IOException {
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        try {
            // 声明 http get 请求
            httpGet = new HttpGet(url);
            updateHeader(headerMap);
            httpGet.setHeaders(headers);

            // 发起请求
            response = httpClient.execute(httpGet);

            int statusCode= response.getStatusLine().getStatusCode();

            if(statusCode == 302) {
                Header header = response.getFirstHeader("Location");
                String newuri = header.getValue();
                httpGet = new HttpGet(newuri);
                response = httpClient.execute(httpGet);
            }

            // 返回响应体的内容
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                    response.getEntity()));

        } finally {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
                response.close();
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    public HttpResult postJson(String url, Map<String, Object> headerMap, String jsonData) throws IOException {
        long time = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            updateHeader(headerMap);
            httpPost.setHeaders(headers);

            if (StringUtils.isNotBlank(jsonData)) {
                httpPost.setEntity(new StringEntity(jsonData, ContentType.APPLICATION_JSON));
            }
            // 发起请求
            response = httpClient.execute(httpPost);
            int statusCode= response.getStatusLine().getStatusCode();
            if(statusCode == 302) {
                Header header = response.getFirstHeader("Location");
                String newuri = header.getValue();
                HttpGet httpGet = new HttpGet(newuri);
                response = httpClient.execute(httpGet);
            }
            duration = System.currentTimeMillis() - time;
            if (response.getEntity() != null) {
                return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(
                        response.getEntity(), CHARSET));
            } else {
                return new HttpResult(response.getStatusLine().getStatusCode(), "");
            }
        } finally {
            if (response != null) {
                EntityUtils.consume(response.getEntity());
                response.close();
            }
            httpPost.releaseConnection();
        }
    }

    private void updateHeader(Map<String, Object> headMap) {
        List<Header> headerList = new ArrayList();
        if (remainHead != null) {
            for (Map.Entry<String, String> entry : remainHead.entrySet()) {
                headerList.add(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        if (headMap != null) {
            for (Map.Entry<String, Object> entry : headMap.entrySet()) {
                headerList.add(new BasicHeader(entry.getKey(), (String) entry.getValue()));
            }
        }
        headers = (Header[]) headerList.toArray(new Header[headerList.size()]);
    }
}
