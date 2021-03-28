package cn.ftf.productblockchain.supervisionnode.bean;


import cn.ftf.productblockchain.supervisionnode.Application;
import cn.ftf.productblockchain.supervisionnode.cache.AddressPool;
import cn.ftf.productblockchain.supervisionnode.websocket.MyClient;
import cn.ftf.productblockchain.supervisionnode.websocket.MyServer;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.HashSet;

/**
 * WebSocketInit
 *
 * @Author 房廷飞
 * @Create 2021-03-19 23:35
 */
@Component
@DependsOn("addressPool")
public class WebSocketInit {

    public static MyServer server;

    @PostConstruct
    public void init() throws Exception {
        server=new MyServer(Application.websocketPort);
        server.startServer();
        HashSet<String> addressPoll = AddressPool.addressPoll;
        for (String s : addressPoll) {
            MyClient client = new MyClient(new URI(s));
            client.connect();
        }
    }
}
