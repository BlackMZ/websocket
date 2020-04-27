package com.websocket.websocket.service.impl;

import com.websocket.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WebSocketService实现类
 * @author 陈琪
 * @create 2019-08-08
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void formatAndSend(@Nonnull String destination, @Nonnull Object payload, String... args) {
        destination = format(destination, args);
        send(destination, payload);
    }

    @Override
    public void send(@Nonnull String destination, @Nonnull Object payload) {
        simpMessagingTemplate.convertAndSend(destination, payload);
    }

    @Override
    public void sendToUser(@Nonnull String destination, @Nonnull Object payload, @Nonnull String... users) {
        sendToUser(destination, payload, Arrays.asList(users));
    }

    @Override
    public void sendToUser(@Nonnull String destination, @Nonnull Object payload, @Nonnull List<String> userList) {
        userList.forEach(userId -> simpMessagingTemplate.convertAndSendToUser(userId, destination, payload));
    }

    @Override
    public Map<String, Object> monitor() {
        Map<String, Object> resultMap = new HashMap<>();
        File file = new File("D:\\zout");
        FilenameFilter filenameFilter = (dir, name) -> name.endsWith(".jpg") || name.endsWith(".txt");
        File[] files = file.listFiles(filenameFilter);
        List<String> picPath = new ArrayList<>();
        List<String> txtPath = new ArrayList<>();
        for (File f : files) {
            if (f.getName().endsWith(".jpg")) {
                picPath.add(f.getPath());
            } else {
                txtPath.add(f.getPath());
            }
        }
        if (!txtPath.isEmpty()) {
            String content = txt2String(txtPath.get(0));
            resultMap.put("description", content);
        }
        if (!picPath.isEmpty()) {
            resultMap.put("paths", picPath);
        }
        return resultMap;
    }


    /**
     * 读取txt内容
     * @param filePath 文件路径
     * @return 文件内容
     */
    private static String txt2String(String filePath){
        File file = new File(filePath);
        StringBuilder result = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)){
            String content;
            while((content = br.readLine())!=null){
                result.append(System.lineSeparator()).append(content);
            }
        }catch(Exception e){
            log.error("读取txt文件内容错误：" + e.getMessage());
        }
        return result.toString();
    }
}