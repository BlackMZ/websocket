package com.websocket.websocket.interceptor;

import com.websocket.websocket.constant.AuthorizationConstants;
import com.websocket.websocket.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;

/**
 * WebSocket输入通道拦截器
 * @author qi.chen
 * @create 2019-08-09
 */
@Slf4j
public class WebSocketInterceptor implements ChannelInterceptor {
    
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        MessageHeaders headers = message.getHeaders();
        StompCommand command = accessor.getCommand();
        // 过滤心跳信息
        if (command == null) {
            return message;
        }
        switch (command) {
            case CONNECT:
                if (!validateToken(accessor)) {
                    return null;
                }
                log.info("CONNECT：Inbound simpSessionId={}", headers.get("simpSessionId"));
                break;
            default:
                break;
        }
        return message;
    }

    /**
     * 验证token
     * @param accessor  Stomp请求头访问器
     * @return  是否有效
     */
    private boolean validateToken(StompHeaderAccessor accessor){
        String token = accessor.getFirstNativeHeader(AuthorizationConstants.AUTHORIZATION);
        if (!StringUtils.hasText(token)) {
            log.error("缺少token");
            return false;
        }
        Claims claims;
        try {
            claims = TokenUtil.parseJWT(token);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("token 过期了");
            return false;
        } catch (Exception exception) {
            log.error("无效 token");
            return false;
        }
        String principalId = claims.getId();
//        if (!redisTemplate.opsForSet().isMember(AuthorizationConstants.REDIS_TOKEN_KEY + principalId, token)) {
//            log.error("用户不存在或已失效，请重新登录");
//            return false;
//        }

        return true;
    }
    
}
