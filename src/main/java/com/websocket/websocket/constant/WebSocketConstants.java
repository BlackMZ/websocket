package com.websocket.websocket.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * WebSocket常量
 * @author qi.chen
 * @create 2019-08-09
 */
public class WebSocketConstants {

    /**
     * Stomp节点，接收客户端的连接
     */
    public static final String ENDPOINT = "/websocket/connect";

    /**
     * 跨域支持
     */
    public static final String[] ALLOWED_ORIGINS;
    static {
        List<String> allowedOriginsList = new ArrayList<>();
        allowedOriginsList.add("*");
        ALLOWED_ORIGINS = allowedOriginsList.toArray(new String[0]);
    }
    
    /**
     * 客户端给服务端发消息的地址前缀，即服务端接收消息的地址前缀
     */
    public static final String APPLICATION_DESTINATION_PREFIXE = "/message";

    /**
     * 通用主题前缀
     */
    public static final String APP_TOPIC_PREFIX = "/app/";
    
    /**
     * xxx某业务 主题前缀
     */
    public static final String PIC_TOPIC_PREFIX = APP_TOPIC_PREFIX + "pic";


    public static final String REPORT_TOPIC_PREFIX = APP_TOPIC_PREFIX + "report";


    /**
     * 用户点对点推送的前缀
     */
    public static final String USER_PTP_PREFIX = "/user/";
}
