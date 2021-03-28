package cn.ftf.productblockchain.supervisionnode.cache;


import cn.ftf.productblockchain.supervisionnode.bean.POJO.ProductInfo;
import org.springframework.stereotype.Component;

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
    private List<ProductInfo> productInfoPool;

    public DataPool(){
        productInfoPool=new ArrayList<>();
    }

    public void addData(ProductInfo productInfo){
        this.productInfoPool.add(productInfo);
        System.out.println("Pool Size:"+this.productInfoPool.size());
    }

}
