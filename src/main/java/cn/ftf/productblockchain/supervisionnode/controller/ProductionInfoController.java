package cn.ftf.productblockchain.supervisionnode.controller;

import cn.ftf.productblockchain.supervisionnode.cache.DataPool;
import cn.ftf.productblockchain.supervisionnode.message.BroadcastMsg;
import cn.ftf.productblockchain.supervisionnode.message.Result;
import cn.ftf.productblockchain.supervisionnode.util.RSAUtils;
import cn.ftf.productblockchain.supervisionnode.websocket.MyServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * ProductionInfoController
 *
 * @Author 房廷飞
 * @Create 2020-12-10 17:31
 */

@RestController
public class ProductionInfoController {

    @Autowired
    private DataPool dataPool;

    Logger logger= LoggerFactory.getLogger(getClass());

//    @RequestMapping(value = "/addProduction", method = RequestMethod.POST)
//    public Result addProduction(@RequestBody ProductInfo productInfo) throws Exception {
//        String productInfojson=null;
//        String productJson=null;
//        logger.info("[录入商品信息] productInfo:"+productInfo);
//        ObjectMapper mapper=new ObjectMapper();
//        try {
//            productInfojson = mapper.writeValueAsString(productInfo);
//            logger.info("[录入商品信息转化为JSON] productInfojson:"+productInfojson);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        try {
//            Product product=new Product(productInfo.getCompany(),productInfo.getProduct(),productInfo.getdate(),productInfo.getOrginPlace(),productInfo.getDescription(),productInfo.getNotes());
//            productJson=mapper.writeValueAsString(product);
//            logger.info("[提取商品信息]productJson:"+productJson);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        boolean boo= RSAUtils.verify("SHA256withRSA", RSAUtils.getPublicKeyFromString("RSA",productInfo.getSenderPublicKey()), productInfojson, productInfo.getSignaturedData());
//        if(boo){
//            dataPool.addData(productInfo);
//            String messageJson = mapper.writeValueAsString(new BroadcastMsg(1, productInfojson));
//            MyServer server = WebSocketController.server;
//            server.broadcast(messageJson);
//            logger.info("[数据校验成功，录入成功] productInfo:"+productInfo);
//            return new Result(true,"录入成功！");
//        }
//        logger.info("[数据校验失败，录入失败] productInfo:"+productInfo);
//        return new Result(false,"录入失败");
//    }

}
