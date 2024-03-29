package cn.ftf.productblockchain.supervisionnode.cache;

import cn.ftf.productblockchain.supervisionnode.bean.POJO.BroadcastedProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接收到的商品数据池
 *
 * @Author 房廷飞
 * @Create 2020-12-10 18:04
 */
@Component
public class DataPool {
    private static List<BroadcastedProductInfo> productInfoPool;
    private static Logger logger= LoggerFactory.getLogger(DataPool.class);
    public DataPool(){
        productInfoPool=new ArrayList<>();
    }

    public static void addData(BroadcastedProductInfo broadcastedProductInfo) throws IOException {
        productInfoPool.add(broadcastedProductInfo);
        int size=productInfoPool.size();
        logger.info("[DataPool数据量]={}",size);
    }
    public static List<BroadcastedProductInfo> getProductInfoPool(){
        return productInfoPool;
    }

}
