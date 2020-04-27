package com.websocket.websocket.service;


import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WebSocket接口
 * @author qi.chen
 * @create 2019-08-12
 */
public interface WebSocketService {

    /**
     * 先替换主题中的占位符，再广播
     * @param destination   主题前缀
     * @param payload   内容
     * @param args    参数，替换主题中的占位符
     */
    void formatAndSend(@Nonnull String destination, @Nonnull Object payload, String... args);

    /**
     * 广播，发送给所有订阅用户
     * @param destination   主题
     * @param payload   内容
     */
    void send(@Nonnull String destination, @Nonnull Object payload);

    /**
     * 点对点推送
     * @param destination   主题
     * @param payload   内容
     * @param users 用户数组
     */
    void sendToUser(@Nonnull String destination, @Nonnull Object payload, @Nonnull String... users);

    /**
     * 发送给指定用户
     * @param destination   主题
     * @param payload   内容
     * @param userList 用户集合
     */
    void sendToUser(@Nonnull String destination, @Nonnull Object payload, @Nonnull List<String> userList);

    /**
     * 格式化字符串，替换占位符
     * 例：/app/{orderId}/{orderSopId}  ->  /app/24fa93e0-daaf-41ed-9ce8-422d7d78d006/f9340857-b4f1-43e3-a812-2eecc3dd4075
     * @param destination   主题前缀
     * @param args  替换占位符的参数
     * @return 主题前缀
     */
    default String format(@Nonnull String destination, @Nonnull String... args){
        String reg = "\\{.*?}";
        Matcher matcher = Pattern.compile(reg).matcher(destination);
        int i = 0;
        while (matcher.find()) {
            String group = matcher.group(0);
            destination = destination.replace(group, args[i++]);
        }
        return destination;
    }


    /**
     * 监听文件夹，获取图片列表和文字
     * @return
     * @throws Exception
     */
    Map<String, Object> monitor();
}
