package com.jnshu.service1;

import com.jnshu.dto1.ContractRO;

public interface ContractService {

    String getContractUnsigned(int type);

    ContractRO getContractReadySign(long id);

    ContractRO getContractById(long id);
}
