package cn.ftf.productblockchain.supervisionnode.broadcastMsgConsumer;

import cn.ftf.productblockchain.supervisionnode.bean.Block;
import cn.ftf.productblockchain.supervisionnode.bean.Blockchain;
import cn.ftf.productblockchain.supervisionnode.bean.MiniBlock;
import cn.ftf.productblockchain.supervisionnode.bean.POJO.BroadcastedProductInfo;
import cn.ftf.productblockchain.supervisionnode.bean.POJO.ProductInfo;
import cn.ftf.productblockchain.supervisionnode.cache.DataPool;
import cn.ftf.productblockchain.supervisionnode.util.JacksonUtils;
import cn.ftf.productblockchain.supervisionnode.util.RSAUtils;
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
            product = new ProductInfo(broadcastedProductInfo.getCompany(), broadcastedProductInfo.getProduct(), broadcastedProductInfo.getTimeStamp(), broadcastedProductInfo.getOrginPlace(), broadcastedProductInfo.getDescription(), broadcastedProductInfo.getNotes());
            productJson = JacksonUtils.objToJson(product);
            logger.info("[提取商品信息]productJson={}",productJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean boo = RSAUtils.verify("SHA256withRSA", RSAUtils.getPublicKeyFromString("RSA", broadcastedProductInfo.getSenderPublicKey()), productJson, broadcastedProductInfo.getSignaturedData());
        if (boo) {
            DataPool.addData(broadcastedProductInfo);
            logger.info("[数据校验成功，录入成功] productInfo={}",product);
            return;
        }
        logger.info("[数据校验失败，录入失败！] productInfo={}",product);

    }
    public static void handleBlockMsg(String broadcastMsgJson){
        Block block = JacksonUtils.jsonToObj(broadcastMsgJson, Block.class);
        MiniBlock miniBlock = null;
        //区块校验
        Blockchain.verifyBlock(block);
        try {
            miniBlock=new MiniBlock(block.height,block.timeStamp,block.hash,block.preHash);
            logger.info("[生成MiniBlock] miniBlock={}", miniBlock);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
