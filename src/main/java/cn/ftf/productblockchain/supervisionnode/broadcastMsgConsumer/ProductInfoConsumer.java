package cn.ftf.productblockchain.supervisionnode.broadcastMsgConsumer;

import cn.ftf.productblockchain.supervisionnode.message.BroadcastMsg;
import cn.ftf.productblockchain.supervisionnode.util.JacksonUtils;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2021-03-27 21:41
 */
public class ProductInfoConsumer {

    public static void handleReceivedBroadcastMsg(String broadcastMsgJson){
        BroadcastMsg broadcastMsg= JacksonUtils.deserialize(broadcastMsgJson,BroadcastMsg.class);
        System.out.println(broadcastMsg);
    }
}
