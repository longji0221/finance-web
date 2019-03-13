package com.cx.finance.biz.service;

import com.cx.finance.biz.bo.*;
import com.cx.finance.dal.domain.FinUserDo;

import java.util.List;
import java.util.Map;


public interface FinThirdApiService {


     void InAccount(FinInAccountBo bo);

     List<Map<String,String>> getBorrowInfoList(FinBorrowBo bo);

     void pangToAccount(FinPangBo bo);

     FinUserDo getUserCommon(FinUserBo bo);

     Map<String,Object> getBorrowDetail(FinBorrowDetailBo bo);

     List<Map<String,String>> getBorrowInfoByLike(FinBorrowBo bo);

}
