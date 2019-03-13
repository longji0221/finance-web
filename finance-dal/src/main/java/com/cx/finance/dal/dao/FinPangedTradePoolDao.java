package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;
import com.cx.finance.dal.query.OfflineBillDataQuery;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-05 19:50:36
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinPangedTradePoolDao extends BaseDao<FinPangedTradePoolDo, Long> {


    FinPangedTradePoolDo getPangedTradePoolDataByTradeNo(@Param("tradeNo")String  tradeNo);

    List<HashMap> getDayReceiveByAlipay(FinRepaymentOfflineTradePoolQuery query);

    List<HashMap> getDayReceive(FinRepaymentOfflineTradePoolQuery query);
}
