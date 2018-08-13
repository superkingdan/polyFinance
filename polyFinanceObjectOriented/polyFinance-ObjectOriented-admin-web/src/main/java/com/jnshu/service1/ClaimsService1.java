package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.ClaimsListRPO;
import com.jnshu.entity.Claims;
import org.springframework.stereotype.Component;

@Component(value = "claimsService1")
public interface ClaimsService1 {
     Page<Claims> getClaimsList(ClaimsListRPO rpo)throws Exception;

    Claims getClaimsById(long id)throws Exception;

    int updateClaims(Claims claims)throws Exception;

    int addClaims(Claims claims)throws Exception;
}
