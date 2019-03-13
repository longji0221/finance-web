package com.cx.finance.biz.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.biz.bo.FinOfflineRepayBo;
import com.cx.finance.biz.service.FinOfflieRepayService;
import com.cx.finance.biz.service.FinPangedTradePoolService;
import com.cx.finance.biz.service.FinUserAccountRecordService;
import com.cx.finance.biz.service.FinUserAccountService;
import com.cx.finance.biz.service.FinUserService;
import com.cx.finance.common.enums.AccountRecordType;
import com.cx.finance.common.enums.CompanyType;
import com.cx.finance.common.enums.InAccountType;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.FinanceThirdUtil;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import com.cx.finance.dal.domain.FinUserAccountDo;
import com.cx.finance.dal.domain.FinUserAccountRecordDo;
import org.springframework.stereotype.Component;

@Component
public class FinOfflieRepayServiceImpl implements FinOfflieRepayService {


    @Resource
    private FinUserAccountService finUserAccountService;

    @Resource
    private FinUserService finUserService;

    @Resource
    private FinUserAccountRecordService finUserAccountRecordService;

    @Resource
    private FinPangedTradePoolService finPangedTradePoolService;

    @Override
    public void offlineRepay(FinOfflineRepayBo bo) {
        Map<String, String> data = new HashMap<>();
        data.put("repayTradeNo", bo.repayTradeNo);
        data.put("borrowNo", bo.borrowNo);
        data.put("repayType", bo.repayType);
        data.put("repayTime", bo.payTime==null?new Date().toString():bo.payTime);
        data.put("operTime", DateUtil.formatDate(new Date(),"yyyyMMdd HH:mm:ss"));
        data.put("repayAmount", bo.repayAmount.toString());
        data.put("repayCardNum", bo.repayCardNum);
        data.put("remark", bo.operator+" " +bo.remark);
        data.put("isAdmin", "Y");
        data.put("isAllFinish", bo.isAllFinish);
        String resultStr =FinanceThirdUtil.offlineRepayFoApi(data,bo.dataUrl.get(bo.companyId));
        Map respBo= (Map) JSONUtils.parse(resultStr);
        if(respBo.get("code").equals("200") ){
            addFinPangedTradePool(InAccountType.OFFLINE_REPAY.getCode(),bo.mobile,bo.repayTradeNo,bo.repayAmount
                    ,bo.operator+" 产品：" +bo.companyId+"借款："+bo.borrowNo);
        }else {
            throw new BizException(respBo.get("msg").toString());
         }
    }


    void addFinPangedTradePool(String type,String accountId,String tradeNo,BigDecimal amount,String remark){
        FinPangedTradePoolDo finPangedTradePoolDo=new FinPangedTradePoolDo();
        finPangedTradePoolDo.setType(type);
        finPangedTradePoolDo.setAccountId(accountId);
        finPangedTradePoolDo.setTradeNo(tradeNo);
        finPangedTradePoolDo.setAmount(amount);
        finPangedTradePoolDo.setRemark(remark);
        finPangedTradePoolService.saveRecord(finPangedTradePoolDo);
    }
}
