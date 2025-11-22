package com.labs339.platform.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.labs339.platform.config.ChainInfoConfig;
import com.labs339.platform.dao.entity.ChainBlockHeightModel;
import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.mapper.ChainBlockHeightMapper;
import com.labs339.platform.enums.ChainTxType;
import com.labs339.platform.rpcclient.ChainClientManager;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
@Component
public class EvmNodeBlockSyncHandler {

    @Autowired
    private ChainClientManager chainClientManager;
    @Autowired
    private ChainInfoConfig chainInfoConfig;
    @Autowired
    private ChainBlockHeightMapper chainBlockHeightMapper;

    /**
     * ETH扫链
     * @throws Exception
     */
    @XxlJob("ethNodeBlockSyncHandler")
    public void ethNodeBlockSyncHandler() throws Exception {

        ChainTxType chainTxType = ChainTxType.Ethereum;
        String chainName = "Eth_Sepolia";



    }

    /**
     * ARB 扫链
     * @throws Exception
     */
    @XxlJob("arbNodeBlockSyncHandler")
    public void arbNodeBlockSyncHandler() throws Exception {

    }

    /**
     * Op 扫链
     * @throws Exception
     */
    @XxlJob("opNodeBlockSyncHandler")
    public void opNodeBlockSyncHandler() throws Exception {

    }


    /**
     * 链上区块与本地最新区块对比
     *
     * 链上区块 <= 本地最新区块
     *  ---不处理，抛出告警
     *
     * 链上区块 > 本地最新区块
     *     异步执行
     *
     * @param chainName
     */
    public void synBlock(String chainName){

        try {
            ChainBlockHeightModel chainBlockHeightModel = chainBlockHeightMapper.selectOne(new LambdaQueryWrapper<ChainBlockHeightModel>().eq(ChainBlockHeightModel::getChainName,chainName));
            Web3j web3j = chainClientManager.getClient(chainName, Web3j.class);
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();


            BigInteger startBlock = blockNumber.getBlockNumber();
            BigInteger endBlock = blockNumber.getBlockNumber();
            if (chainBlockHeightModel == null) {
                ChainConfigModel config = chainInfoConfig.getChainConfigMap().get(chainName);
                if (config == null) {
                    log.error("chain config not found");
                    return;
                }
                EthBlock block = handlerTx(chainName,blockNumber.getBlockNumber(),web3j);
                chainBlockHeightModel = new ChainBlockHeightModel();
                chainBlockHeightModel.setChainName(config.getChainName());
                chainBlockHeightModel.setChainConfigId(config.getId());
                chainBlockHeightModel.setChainId(config.getChainId());
                chainBlockHeightModel.setCurrentBlockHash(block.getBlock().getHash());
                chainBlockHeightModel.setCurrentBlockNum(blockNumber.getBlockNumber().longValue());
                chainBlockHeightMapper.insert(chainBlockHeightModel);
                return;
            }else {



            }




            EthBlock currentBlock = null;

            BigInteger currentBlockNumber =null;
            if(chainBlockHeightModel ==null){
                currentBlockNumber = blockNumber.getBlockNumber();
            }else {
                currentBlockNumber = BigInteger.valueOf(chainBlockHeightModel.getCurrentBlockNum()).add(BigInteger.ONE);
            }
            EthBlock block = web3j.ethGetBlockByNumber(
                    DefaultBlockParameter.valueOf(currentBlockNumber),
                    true // true 表示包含交易详情, false 返回 交易哈希
            ).send();


            String currentBlockHash = null;


            chainBlockHeightModel.setLatestBlockNum(blockNumber.getBlockNumber().longValue());
            chainBlockHeightModel.setCurrentBlockNum(currentBlock.getBlock().getNumber().longValue());
            chainBlockHeightModel.setCurrentBlockHash(currentBlock.getBlock().getHash());

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }


    public void handlerBlock(String chainName,BigInteger startBlockNumber,BigInteger endBlockNumber){




    }


    /**
     * ethGetBlockReceipts 使用注意 轻节点（light mode） 或 snap sync 模式下的执行层节点，receipt 数据可能还没完全同步
     * @param chainName
     * @param blockNumber
     * @param web3j
     * @return
     * @throws IOException
     */
    public EthBlock handlerTx(String chainName,BigInteger blockNumber,Web3j web3j) throws IOException {

        Map<String,String> txSuccessReceipt = new HashMap<>();

        DefaultBlockParameter blockParameter = DefaultBlockParameter.valueOf(blockNumber);
        EthBlock block = web3j.ethGetBlockByNumber(
                blockParameter,
                true // true 表示包含交易详情, false 返回 交易哈希
        ).send();


        // 先处理 Receipts
        EthGetBlockReceipts blockReceipts = web3j.ethGetBlockReceipts(blockParameter)
                .send();
        Optional<List<TransactionReceipt>> optional = blockReceipts.getBlockReceipts();
        if (!optional.isPresent()) {
            // 交易为空
            return block;
        }
        for (TransactionReceipt receipt : optional.get()) {
            if (!receipt.isStatusOK()){
                continue;
            }
            txSuccessReceipt.put(receipt.getTransactionHash(), receipt.getGasUsed().toString());

            // 处理合约
            List<Log> logs = receipt.getLogs();
            if (CollectionUtils.isEmpty(logs)) {
                continue;
            }
            for (Log log : logs) {

                // todo redis bloomfilter 过滤 合约地址
                String tokenAddress = log.getAddress().toLowerCase();

                List<String> topics = log.getTopics();
                if (CollectionUtils.isEmpty(topics)) {
                    continue;
                }
                String data = log.getData();




            }


        }



        List<EthBlock.TransactionResult> transactionResults = block.getBlock().getTransactions();
        if (CollectionUtils.isEmpty(transactionResults)) {
            // 交易为空，更新区块数据
            return block;
        }

        for (EthBlock.TransactionResult transactionResult : transactionResults) {
            Transaction transaction = (Transaction) transactionResult.get();
            BigInteger value = transaction.getValue();
            if (value == null || value.compareTo(BigInteger.ZERO) <= 0) {
                // 交易金额小于等于 0
                return null;
            }
            String from = transaction.getFrom();
            String to = transaction.getTo();
            // todo redis bloom filter  原生代币 入账

            transaction.getValue();


        }

        return block;

    }



    public void handlerTxEvent(String chainName,BigInteger blockNumber,Web3j web3j) throws IOException {

        EthBlock block = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(blockNumber),
                true // true 表示包含交易详情, false 返回 交易哈希
        ).send();
        List<EthBlock.TransactionResult> transactionResults = block.getBlock().getTransactions();
        for (EthBlock.TransactionResult transactionResult : transactionResults) {
            Transaction transaction = (Transaction) transactionResult.get();
            BigInteger value = transaction.getValue();
            if (value == null || value.compareTo(BigInteger.ZERO) <= 0) {
                // 交易金额小于等于 0
                return;
            }
            String from = transaction.getFrom();
            String to = transaction.getTo();
            // todo redis bloom filter  原生代币 入账

            // handler contract event
            EthFilter filter = new EthFilter(
                    DefaultBlockParameter.valueOf(blockNumber), // 起始区块
                    DefaultBlockParameter.valueOf(blockNumber), // 结束区块
                    Arrays.asList(
                            "0xContractAddress1",
                            "0xContractAddress2",
                            "0xContractAddress3"
                    )                 // 合约地址
            );
            EthLog logs = web3j.ethGetLogs(filter).send();
            logs.getLogs().forEach(logResult -> {

            });


        }

    }


}
