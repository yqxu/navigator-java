package com.pingpongx.smb.warning.web.filter;

import com.alibaba.fastjson.JSON;
import com.pingpongx.business.common.exception.BizException;
import com.pingpongx.business.common.exception.ErrorCode;
import com.pingpongx.common.web.util.IpUtil;
import com.pingpongx.smb.warning.web.constant.X;
import com.pingpongx.smb.warning.web.filter.warpper.RequestWrapper;
import com.pingpongx.smb.warning.web.helper.BusinessAlertVerify;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:36 下午
 * @Description:
 * @Version:
 */
@Slf4j
public class WarningWebFilter extends OncePerRequestFilter {

    @Autowired
    private BusinessAlertVerify businessAlertVerify;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        if (request.getRequestURI().contains("/v1/smb/dingDing")) {
            boolean b = businessAlertVerify.checkRequest(request.getHeader(X.DINGDING_ALERT_HEADER_NAME));
            if (!b) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Signature verification fails");
                return;
            }
        }
        // 读数据从初始化的body取 (只会读取一次原始流)
        RequestWrapper multipleReadBodyRequestWrapper = new RequestWrapper(request);
        // 写数据后到缓存 (取都是缓存, 请求完需要 调用 copyBodyToResponse() 将缓存的body写到真正的响应体里并输出)
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(multipleReadBodyRequestWrapper, contentCachingResponseWrapper);

        String responseBody = getResponseBody(contentCachingResponseWrapper);
        //copyBodyToResponse() 执行前 可以拿到body 但拿不到header  执行后 可以拿到header 但是拿不到body
        contentCachingResponseWrapper.copyBodyToResponse();
        saveHttpLog(multipleReadBodyRequestWrapper, contentCachingResponseWrapper, responseBody, start);

    }

    private void saveHttpLog(RequestWrapper request, ContentCachingResponseWrapper response, String responseBodyStr, long start) {
        String httpUri = request.getMethod() + ":" + request.getRequestURI();
        String urlParams = getRequestParams(request);
        String requestBodyStr = request.getBody();
        int statusCode = response.getStatus();
        log.info("请求接口:{}|{}|{}| {}ms | {} | >>>>:{} | <<<<:{}", httpUri, statusCode, IpUtil.getRemoteAddr(request),
            System.currentTimeMillis() - start, urlParams, requestBodyStr, responseBodyStr.substring(0, Math.min(responseBodyStr.length(), 1024)));
    }

    /**
     * 获取请求地址上的参数
     *
     * @param request ~
     * @return ~
     */
    private String getRequestParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> enu = request.getParameterNames();
        //获取请求参数
        sb.append("[");
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            sb.append(name).append(X.SPLIT_STRING_M).append(request.getParameter(name));
            if (enu.hasMoreElements()) {
                sb.append(X.SPLIT_STRING_DOT);
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 打印请求参数
     *
     * @param response ~
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return buildString(buf, response.getCharacterEncoding());
    }

    private String buildString(byte[] buf, String encoding) {
        if (buf.length == 0) {
            return "";
        }
        try {
            return new String(buf, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new BizException(ErrorCode.BIZ_BREAK, "Unsupported encoding");
        }
    }

    /**
     * 获取请求的Headers
     *
     * @param request ~
     * @return ~
     */
    private String getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return JSON.toJSONString(headerMap, true);
    }
}
