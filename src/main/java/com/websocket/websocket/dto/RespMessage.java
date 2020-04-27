package com.websocket.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 响应消息DTO
 * @author qi.chen
 * @create 2019-08-08
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RespMessage {

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 发送人id
     */
    private String fromUserId;

    /**
     * 接收人id
     */
    private String toUserId;

    /**
     * 聊天室id
     */
    private String chatId;
}
