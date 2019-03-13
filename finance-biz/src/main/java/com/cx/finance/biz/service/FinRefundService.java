package com.cx.finance.biz.service;

import com.cx.finance.biz.bo.FinRefundBo;
import com.cx.finance.dal.domain.FinRefundDo;
import com.cx.finance.dal.domain.FinRefundDto;
import com.cx.finance.dal.query.FinRefundQuery;

import java.util.List;

/**
 * Service
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-09 16:07:21
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinRefundService extends ParentService<FinRefundDo, Long>{

    List<FinRefundDto> getRefundByUserId(FinRefundQuery query);

    List<FinRefundDo> getRefundListByBorrowNo(String borrowNo);

    List<FinRefundDo> getRefundListNoFailByBorrowNo(String borrowNo);

    void executeRefund(FinRefundBo bo);

    void refuseRefund(FinRefundBo bo);

    void passRefund(FinRefundBo bo);

}
