package cn.ftf.productblockchain.supervisionnode.broadcastMsgConsumer;

import cn.ftf.productblockchain.supervisionnode.bean.POJO.BroadcastedProductInfo;
import cn.ftf.productblockchain.supervisionnode.bean.POJO.ProductInfo;
import cn.ftf.productblockchain.supervisionnode.cache.DataPool;
import cn.ftf.productblockchain.supervisionnode.controller.WebSocketController;
import cn.ftf.productblockchain.supervisionnode.message.BroadcastMsg;
import cn.ftf.productblockchain.supervisionnode.message.Result;
import cn.ftf.productblockchain.supervisionnode.util.JacksonUtils;
import cn.ftf.productblockchain.supervisionnode.util.RSAUtils;
import cn.ftf.productblockchain.supervisionnode.websocket.MyServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2021-03-28 14:29
 */
public class BroadcastMsgConsumer {

    private static Logger logger = LoggerFactory.getLogger(BroadcastMsgConsumer.class);

    public static void handleProductMsg(String broadcastMsgJson) {
        BroadcastedProductInfo broadcastedProductInfo = JacksonUtils.jsonToObj(broadcastMsgJson, BroadcastedProductInfo.class);
        String productJson = null;
        ProductInfo product = null;
        try {
            product = new ProductInfo(broadcastedProductInfo.getCompany(), broadcastedProductInfo.getProduct(), broadcastedProductInfo.getProductionDate(), broadcastedProductInfo.getOrginPlace(), broadcastedProductInfo.getDescription(), broadcastedProductInfo.getNotes());
            productJson = JacksonUtils.objToJson(product);
            logger.info("[提取商品信息]productJson:" + productJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean boo = RSAUtils.verify("SHA256withRSA", RSAUtils.getPublicKeyFromString("RSA", broadcastedProductInfo.getSenderPublicKey()), productJson, broadcastedProductInfo.getSignaturedData());
        if (boo) {
            DataPool.addData(product);
            logger.info("[数据校验成功，录入成功] productInfo:" + product);
            return;
        }
        logger.info("[数据校验失败，录入失败！] productInfo:" + product);

    }
}
