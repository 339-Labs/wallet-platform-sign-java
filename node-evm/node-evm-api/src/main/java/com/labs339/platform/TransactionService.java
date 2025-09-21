package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;
import org.apache.ibatis.transaction.Transaction;

public interface TransactionService {

    CommonResponse SendTransaction(String signData,String chainName);

    CommonResponse GetTransactionByTxHash(String hash,String chainName);

}
