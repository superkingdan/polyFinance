package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.ClaimsListRPO;
import com.jnshu.entity.Claims;

public interface ClaimsService {
     Page<Claims> getClaimsList(ClaimsListRPO rpo);

    Claims getClaimsById(long id);

    int updateClaims(Claims claims);

    int addClaims(Claims claims);
}
