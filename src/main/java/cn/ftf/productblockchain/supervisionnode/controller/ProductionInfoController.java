package cn.ftf.productblockchain.supervisionnode.controller;

import cn.ftf.productblockchain.supervisionnode.bean.Message;
import cn.ftf.productblockchain.supervisionnode.bean.Product;
import cn.ftf.productblockchain.supervisionnode.bean.ProductInfo;
import cn.ftf.productblockchain.supervisionnode.cache.DataPool;
import cn.ftf.productblockchain.supervisionnode.message.Result;
import cn.ftf.productblockchain.supervisionnode.util.RSAUtils;
import cn.ftf.productblockchain.supervisionnode.websocket.MyServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @RequestMapping(value = "/addProduction", method = RequestMethod.POST)
    public Result addProduction(@RequestBody ProductInfo productInfo) throws Exception {
        String orginJson=null;
        String productInfojson=null;
        System.out.println("productInfo:\n"+productInfo);
        ObjectMapper mapper=new ObjectMapper();
        try {
            orginJson = mapper.writeValueAsString(productInfo);
            System.out.println("orginJson:\n"+orginJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            Product product=new Product(productInfo.getCompany(),productInfo.getProduct(),productInfo.getProductionDate(),productInfo.getOrginPlace(),productInfo.getDescription());
            productInfojson=mapper.writeValueAsString(product);
            System.out.println("productInfojson:\n"+productInfojson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        boolean boo= RSAUtils.verify("SHA256withRSA", RSAUtils.getPublicKeyFromString("RSA",productInfo.getSenderPublicKey()), productInfojson, productInfo.getSignaturedData());
        if(boo){
            dataPool.addData(productInfo);
            String messageJson = mapper.writeValueAsString(new Message(1, orginJson));
//            System.out.println("messageJson:\n"+messageJson);
//            String strMessage = mapper.readValue(messageJson, Message.class).toString();
//            strMessage.replaceAll("\\\\n","\r\n");
//            System.out.println("strMessage:\n"+strMessage);
            MyServer server = WebSocketController.server;
            server.broadcast(messageJson);
            return new Result(true,"录入成功！");
        }
        return new Result(false,"录入失败");
    }

}
