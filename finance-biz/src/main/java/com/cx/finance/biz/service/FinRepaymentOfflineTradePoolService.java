package com.cx.finance.biz.service;

import com.cx.finance.dal.domain.FinRepaymentOfflineTradePoolDo;
import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;

import java.util.HashMap;
import java.util.List;

/**
 * Service
 * 
 * @author yinxiangyu
 * @version 1.0.0 初始化
 * @date 2018-10-30 11:43:44
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinRepaymentOfflineTradePoolService extends ParentService<FinRepaymentOfflineTradePoolDo, Long>{

    //导入数据库
    int saveRecord(FinRepaymentOfflineTradePoolDo finRepaymentOfflineTradePoolDo);

    List<FinRepaymentOfflineTradePoolQuery> getListRepaymentOfflinePool(FinRepaymentOfflineTradePoolQuery query);

    List<FinRepaymentOfflineTradePoolQuery> offlineRepaymentDataQuery(FinRepaymentOfflineTradePoolQuery query);

    FinRepaymentOfflineTradePoolDo getRepaymentOfflinePoolDataByTradeNo(String tradeNo);

    List<HashMap> getDayAmountByAlipay(FinRepaymentOfflineTradePoolQuery query);

    List<HashMap> getDayAmount(FinRepaymentOfflineTradePoolQuery query);

    List<HashMap> getNoUseTradesByMobile(String mobile);

}
