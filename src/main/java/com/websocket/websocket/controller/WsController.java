package com.websocket.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.websocket.websocket.constant.AuthorizationConstants;
import com.websocket.websocket.constant.WebSocketConstants;
import com.websocket.websocket.dto.ReqMessage;
import com.websocket.websocket.dto.RespMessage;
import com.websocket.websocket.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket控制器（订阅方式）
 *
 * @author qi.chen
 * @create 2019-08-08
 */
@Controller
@Slf4j
public class WsController {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * @param reqMessage 请求消息DTO
     * @MessageMapping：和@RequestMapping功能类似，用于设置URL映射地址。
     * @SendTo：如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息
     */
    @MessageMapping("/sendAll")
    @SendTo("/app/all")
    public RespMessage sendAll(@Payload ReqMessage reqMessage, @Header("simpSessionId") String sessionId, StompHeaderAccessor accessor) {
        Object principalId = accessor.getSessionAttributes().get(AuthorizationConstants.CURRENT_USER_ID);
        System.out.println("getSessionAttributes:" + principalId);

        System.out.println("simpSessionId：" + sessionId);
        return new RespMessage().setMsg("Hello everyone! " + reqMessage.getMsg() + "!");
    }

    @MessageMapping("/sendTopic")
    public void sendTopic(ReqMessage reqMessage) {

        RespMessage respMessage = new RespMessage();
        respMessage.setMsg("Hello xxx某业务主题!").setFromUserId(reqMessage.getFromUserId());
        //webSocketService.send(WebSocketConstants.PIC_TOPIC_PREFIX, respMessage);

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        scheduledThreadPool.schedule(() -> {
                System.out.println("delay 5 seconds");
                Map<String, Object> monitor = webSocketService.monitor();
                webSocketService.send(WebSocketConstants.PIC_TOPIC_PREFIX, monitor);
            }, 5, TimeUnit.SECONDS);


//        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
//        scheduledThreadPool.scheduleAtFixedRate(() -> {
//            webSocketService.send(WebSocketConstants.PIC_TOPIC_PREFIX, respMessage);
//            System.out.println("delay 1 seconds, and excute every 3 seconds");
//        }, 5, 3, TimeUnit.SECONDS);
    }

    @MessageMapping("/sendToUsers")
    public void sendToUsers(ReqMessage message) {
        String userId = "d892bf12bf7d11e793b69c5c8e6f60fb";
        RespMessage respMessage = new RespMessage()
                .setToUserId(userId).setMsg("sendToUsers");
        webSocketService.sendToUser("/stationLetter", respMessage, userId);
    }

    @RequestMapping("/sendAll")
    @ResponseBody
    public String httpSendAll() {
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
//        final String jobID = "get_pic_1";
//        final AtomicInteger count = new AtomicInteger(0);
//        final Map<String, Future> futures = new HashMap<>();
//
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        Future future = scheduler.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(count.getAndIncrement());
//                Map<String, Object> monitor = webSocketService.monitor();
//                webSocketService.send(WebSocketConstants.PIC_TOPIC_PREFIX, monitor);
//                if (count.get() > 5) {
//                    Future future = futures.get(jobID);
//                    if (future != null) future.cancel(true);
//                    countDownLatch.countDown();
//                }
//            }
//        }, 0, 1, TimeUnit.SECONDS);
//
//        futures.put(jobID, future);
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        scheduler.shutdown();


          ScheduledExecutorService scheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        scheduledThreadPool.schedule(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(3000);
                    System.out.println("delay 5 seconds" + Thread.currentThread().getId());
                    Map<String, Object> monitor = webSocketService.monitor();
                    webSocketService.send(WebSocketConstants.PIC_TOPIC_PREFIX, monitor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);

        //webSocketService.send("/app/all", result);
        return "发送成功1";
    }

    /**
     * 处理发送消息的异常
     *
     * @param exception 异常
     * @param msg       客户端发送的消息
     */
    @MessageExceptionHandler
    public void messageExceptionHandler(Exception exception, String msg) {
        log.error("Error handling message: " + exception.getMessage());
        ReqMessage reqMessage = JSON.parseObject(msg, ReqMessage.class);
        //把错误信息发回给发送人
        simpMessagingTemplate.convertAndSendToUser(reqMessage.getFromUserId(),
                "/queue/contactErrors" + reqMessage.getChatId(), exception.getMessage());
    }
}
