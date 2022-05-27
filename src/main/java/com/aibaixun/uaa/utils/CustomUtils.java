package com.aibaixun.uaa.utils;

import com.aibaixun.common.util.JsonUtil;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class CustomUtils {
    /**
     * 响应json数据给前端
     *
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) {
        try {
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            //Java对象转为Json格式的数据(objectMapper.writeValueAsString)
            writer.print(JsonUtil.toJSONString(obj));
            writer.close();
            //内容写到客户端浏览器
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
