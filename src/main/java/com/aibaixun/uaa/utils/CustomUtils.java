package com.aibaixun.uaa.utils;

import com.aibaixun.common.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;

public class CustomUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * 响应json数据给前端
     *
     * @param response response
     * @param obj obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {
        try {
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(JsonUtil.toJSONString(obj));
            writer.close();
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String,String> formatRequestBody( HttpServletRequest request){
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("不支持该认证身份方法");
        }
        HashMap<String,String> longinUser;
        try {
            longinUser = OBJECT_MAPPER.readValue(request.getReader(), new TypeReference<>() {});

        } catch (Exception e) {
            throw new AuthenticationServiceException("错误请求");
        }
        return longinUser;
    }

}
