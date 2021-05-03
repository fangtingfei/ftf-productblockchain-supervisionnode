package cn.ftf.productblockchain.supervisionnode.broadcastMsgConsumer;


import cn.ftf.productblockchain.supervisionnode.bean.POJO.BroadcastedProductInfo;
import cn.ftf.productblockchain.supervisionnode.bean.POJO.ProductInfo;
import cn.ftf.productblockchain.supervisionnode.bean.block.Block;
import cn.ftf.productblockchain.supervisionnode.bean.block.Blockchain;
import cn.ftf.productblockchain.supervisionnode.bean.block.MiniBlock;
import cn.ftf.productblockchain.supervisionnode.cache.DataPool;
import cn.ftf.productblockchain.supervisionnode.message.BroadcastMsg;
import cn.ftf.productblockchain.supervisionnode.util.JacksonUtils;
import cn.ftf.productblockchain.supervisionnode.util.RSAUtils;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2021-03-28 14:29
 */
public class BroadcastMsgConsumer {

    private static Logger logger = LoggerFactory.getLogger(BroadcastMsgConsumer.class);

    public static void handleProductMsg(String broadcastMsgJson) throws IOException {
        BroadcastedProductInfo broadcastedProductInfo = JacksonUtils.jsonToObj(broadcastMsgJson, BroadcastedProductInfo.class);
        String productJson = null;
        ProductInfo product = null;
        try {
            product = new ProductInfo(broadcastedProductInfo.getCompany(), broadcastedProductInfo.getProduct(), broadcastedProductInfo.getTimeStamp(), broadcastedProductInfo.getOrginPlace(), broadcastedProductInfo.getDescription(), broadcastedProductInfo.getNotes());
            productJson = JacksonUtils.objToJson(product);
            logger.info("[提取商品信息]productJson:" + productJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean boo = RSAUtils.verify("SHA256withRSA", RSAUtils.getPublicKeyFromString("RSA", broadcastedProductInfo.getSenderPublicKey()), productJson, broadcastedProductInfo.getSignaturedData());
        if (boo) {
            DataPool.addData(broadcastedProductInfo);
            logger.info("[数据校验成功，录入成功] productInfo:" + product);
            return;
        }
        logger.info("[数据校验失败，录入失败！] productInfo:" + product);

    }

    public static void handleBlockMsg(String broadcastMsgJson, WebSocketClient client) {
        Block block = JacksonUtils.jsonToObj(broadcastMsgJson, Block.class);
        MiniBlock miniBlock = null;
        //区块校验
        boolean boo = Blockchain.verifyBlock(block);
        if (!boo) {
            logger.info("[接收的区块验证失败] block={}", block);
        }
        System.out.println("[接收的区块验证成功]");
        //TODO
        //验证成功，待补充
        Boolean matchFlag=true;
        HashSet<String> localHashSet = new HashSet<>();
        for (int i = 0; i < DataPool.getProductInfoPool().size(); i++) {
            localHashSet.add(JacksonUtils.objToJson(DataPool.getProductInfoPool().get(i)));
        }
        for (int i = 0; i < block.getList().length; i++) {
            if (!localHashSet.contains(JacksonUtils.objToJson(block.getList()[i]))) {
                System.out.println("本地数据池不含有该条数据！");
                matchFlag=false;
            } else {
                System.out.println("匹配到本地数据池数据" + JacksonUtils.objToJson(block.getList()[i]));
            }
        }
        if(matchFlag){
            BroadcastMsg msg=new BroadcastMsg(3,"VOTE");
            client.send(JacksonUtils.objToJson(msg));
        }

    }
    public static void handleViewedBlockMsg(String broadcastMsgJson) throws IOException {
        Block block = JacksonUtils.jsonToObj(broadcastMsgJson, Block.class);
        MiniBlock miniBlock=null;
        try {
            miniBlock = new MiniBlock(block.height, block.timeStamp, block.hash, block.preHash);
            logger.info("[生成MiniBlock] miniBlock={}", miniBlock);
            Blockchain.addBlock(miniBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 4; i++) {
            DataPool.getProductInfoPool().remove(0);
        }
    }

}


