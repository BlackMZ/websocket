package com.websocket.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }
    
/*    public void test(){
        // 拍照接口
        // 1.调曹诗接口

        // 2.开启一个异步线程（池）
        new Thread((new Runnable() {
            @Override
            public void run() {
                隐患：如果4个照片片，1张的时候返回，GG
                while(true){
                    // 扫描文件
                    if (扫描到文件) {
                        // 推送给前端某个频道。（图片数据）
                        break;
                    }
                }
            }
        })).start();
        // 3.返回200给前端
    }
    // 4.前端收到频道的消息，再处理业务逻辑
    */

}
