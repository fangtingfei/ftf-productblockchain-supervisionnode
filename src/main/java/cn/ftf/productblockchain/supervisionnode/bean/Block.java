package cn.ftf.productblockchain.supervisionnode.bean;

import cn.ftf.productblockchain.supervisionnode.bean.POJO.ProductInfo;

import java.util.List;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2021-03-13 22:33
 */
public class Block {
    public Integer id;
    public String date;
    public List<ProductInfo> data;
    public String hash;
    public String preHash;
}
