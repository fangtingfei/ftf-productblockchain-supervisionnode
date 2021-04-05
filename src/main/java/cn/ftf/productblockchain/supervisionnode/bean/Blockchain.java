package cn.ftf.productblockchain.supervisionnode.bean;

import cn.ftf.productblockchain.supervisionnode.util.JacksonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2021-04-05 11:55
 */
public class Blockchain {
    private static List<MiniBlock> blocks=new ArrayList<>();

    @PostConstruct
    private void init() throws Exception {
        File blockChainDB = new File("blockchain.db");
        if (!blockChainDB.exists() || blockChainDB.length() == 0 || !blockChainDB.exists() || blockChainDB.length() == 0) {
            blockChainDB.createNewFile();
        }
        List<String> blocks = FileUtils.readLines(blockChainDB, "UTF-8");
        if(blocks.size()==0){
            System.out.println("监督节点区块链初始化失败-没有区块,请检出区块DB");
        }
        blocks.stream().forEach(blockJson->initBlockChainFromDB(blockJson));
        System.out.println("监督节点区块链初始化成功，size="+blocks.size());
    }

    /**
     * 从 DB 中恢复区块链数据
     *
     * @return
     */

    private static void initBlockChainFromDB(String blockJson) {
        if(blockJson.isBlank()) {return;}
        MiniBlock block=null;
        try {
            block = JacksonUtils.jsonToObj(blockJson, MiniBlock.class);
        }catch (Exception e){
            System.out.println("Mini区块反序列化失败:"+blockJson);
        }

        blocks.add(block);
    }



    /**
     * 区块合法性验证
     */
    public static boolean verifyBlock(Block block) {
        int currentHeight= block.height;
        if(currentHeight==0){return true;}
        MiniBlock preBlock=blocks.get(currentHeight-1);
        String preHash= preBlock.hash;
        if(!preHash.equals(block.getPreHash())){
            System.out.println("prehash验证不合法");
            return false;
        }
        //区块Hash校验
        return verifyBlockData(block);
    }

    /**
     * 区块数据验证
     */
    public static boolean verifyBlockData(Block block){
        byte[] prevBlockHashBytes = {};
        if (StringUtils.isNoneBlank(block.getPreHash())) {
            prevBlockHashBytes = new BigInteger(block.getPreHash(), 16).toByteArray();
        }

        byte[] data =ByteUtils.merge(
                prevBlockHashBytes,
                //所有商品信息的默克尔Hash
                block.hashTransaction(),
                ByteUtils.toBytes(block.timeStamp)
        );
        String shaHex = DigestUtils.sha256Hex(data);
        if(!block.hash.equals(shaHex)){
            System.out.println("区块Hash校验失败");
            return false;
        }
        return true;
    }



    public static MiniBlock getBlockByHeight(int height){
        return blocks.get(height);
    }

    /**
     * 全链校验
     * @return
     */
    public static boolean verifyAllBlockChain() throws Exception {
        for (int i = 0; i < blocks.size(); i++) {
            if(!verifyBlock(blocks.get(i))){
                System.out.println("全链校验失败，失败目标block"+blocks.get(i));
                return false;
            }
        }
        return true;
    }







    public List<MiniBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<MiniBlock> blocks) {
        blocks = blocks;
    }
}
