package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.ClaimsListRPO;
import com.jnshu.entity.Claims;
import org.springframework.stereotype.Component;

@Component(value = "claimsService1")
public interface ClaimsService1 {
     Page<Claims> getClaimsList(ClaimsListRPO rpo);

    Claims getClaimsById(long id);

    int updateClaims(Claims claims);

    int addClaims(Claims claims);
}
