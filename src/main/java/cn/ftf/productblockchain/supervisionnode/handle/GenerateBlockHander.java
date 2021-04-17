package cn.ftf.productblockchain.supervisionnode.handle;

import cn.ftf.productblockchain.supervisionnode.bean.WebSocketInit;
import cn.ftf.productblockchain.supervisionnode.bean.block.Block;
import cn.ftf.productblockchain.supervisionnode.message.BroadcastMsg;
import cn.ftf.productblockchain.supervisionnode.util.JacksonUtils;
import cn.ftf.productblockchain.supervisionnode.websocket.MyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author fangtingfei
 * @version 1.0
 * @date 2021-04-03 11:21
 */
public class GenerateBlockHander {
    Logger logger= LoggerFactory.getLogger(getClass());
    private MyServer server= WebSocketInit.server;

    public void broadcastBlock(Block block){
        String blockJson = JacksonUtils.objToJson(block);
        logger.info("[新区块转化为JSON] blockJson={}",blockJson);
        BroadcastMsg broadcastMsg=new BroadcastMsg(1,blockJson);
        String broascastMsgJson = JacksonUtils.objToJson(broadcastMsg);
        logger.info("[生成区块广播体JSON] broascastMsgJson={}",broascastMsgJson);
        server.broadcast(broascastMsgJson);
    }
}
