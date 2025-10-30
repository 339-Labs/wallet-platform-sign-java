package com.labs339.platform.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.labs339.platform.config.ChainInfoConfig;
import com.labs339.platform.dao.entity.ChainBlockHeightModel;
import com.labs339.platform.dao.mapper.NodeBlockSyncMapper;
import com.labs339.platform.enums.ChainTxType;
import com.labs339.platform.rpcclient.ChainClientManager;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class EvmNodeBlockSyncHandler {

    @Autowired
    private ChainClientManager chainClientManager;
    @Autowired
    private ChainInfoConfig chainInfoConfig;
    @Autowired
    private NodeBlockSyncMapper nodeBlockSyncMapper;

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


    public void handlerBlock(String chainName){

        try {
            ChainBlockHeightModel chainBlockHeightModel = nodeBlockSyncMapper.selectOne(new LambdaQueryWrapper<ChainBlockHeightModel>().eq(ChainBlockHeightModel::getChainName,chainName));
            Web3j web3j = chainClientManager.getClient(chainName, Web3j.class);
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
            if(chainBlockHeightModel ==null){


            }else {

            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }


    public void handlerTx(String chainName,BigInteger blockNumber,Web3j web3j) throws IOException {

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
