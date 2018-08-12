package com.jnshu.service1;

import com.jnshu.dto1.ContractRO;

public interface ContractService1 {

    String getContractUnsigned(int type)throws Exception;

    ContractRO getContractReadySign(long id)throws Exception;

    ContractRO getContractById(long id)throws Exception;
}
