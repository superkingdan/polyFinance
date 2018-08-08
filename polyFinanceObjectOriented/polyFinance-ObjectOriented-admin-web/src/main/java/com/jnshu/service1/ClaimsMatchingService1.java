package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.ClaimsMatchingRO;
import com.jnshu.dto1.ClaimsMatchingRPO;
import com.jnshu.entity.Claims;
import com.jnshu.dto1.ContractMatchingRO;
import com.jnshu.entity.ClaimsMatching;
import org.springframework.stereotype.Component;


import java.util.List;

@Component(value = "claimsMatchingService1")
public interface ClaimsMatchingService1 {
    Claims getClaimsInfoById(long claimsId);

    Page<ClaimsMatchingRO> getClaimsMatchingListByRpo(ClaimsMatchingRPO rpo);

    List<ContractMatchingRO> getClaimsMatchingListById(long claimsId);

    int saveClaimsMatching(ClaimsMatching claimsMatching);
}
