package cn.ftf.productblockchain.supervisionnode.controller;

import cn.ftf.productblockchain.supervisionnode.Application;
import cn.ftf.productblockchain.supervisionnode.cache.AddressPool;
import cn.ftf.productblockchain.supervisionnode.websocket.MyClient;
import cn.ftf.productblockchain.supervisionnode.websocket.MyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.HashSet;

/**
 * WebSocketController
 *
 * @Author 房廷飞
 * @Create 2020-12-19 8:49
 */
@RestController
public class WebSocketController {
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
