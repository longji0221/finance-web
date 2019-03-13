package com.cx.finance.biz.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.biz.bo.*;
import com.cx.finance.biz.service.*;
import com.cx.finance.common.enums.AccountRecordType;
import com.cx.finance.common.enums.CompanyType;
import com.cx.finance.common.enums.InAccountType;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.util.FinanceThirdUtil;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import com.cx.finance.dal.domain.FinUserAccountDo;
import com.cx.finance.dal.domain.FinUserAccountRecordDo;
import com.cx.finance.dal.domain.FinUserDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Component("finThirdApiService")
public class FinThirdApiServiceImpl implements FinThirdApiService {

    protected final Logger logger = Logger.getLogger(getClass());



    @Resource
    private FinUserService finUserService;

    @Resource
    private FinUserAccountService finUserAccountService;

    @Resource
    private FinUserAccountRecordService finUserAccountRecordService;

    @Resource
    private FinPangedTradePoolService finPangedTradePoolService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private FinRefundService finRefundService;

    private final static String SUCCESS_CODE="200";

    @Override
    public void InAccount(FinInAccountBo bo){
        String  resultStr =FinanceThirdUtil.requestInAccountForApi(bo.data,bo.dataUrl.get(bo.produce));
        Map respBo= (Map) JSONUtils.parse(resultStr);
        if(respBo.get("code").equals("200")){
            FinUserDo userDo=finUserService.getUserByMobile(bo.mobile);
            FinUserAccountDo finUserAccountDo=new FinUserAccountDo();
            finUserAccountDo.setTotalAmount(new BigDecimal(bo.accountAmount));
            finUserAccountDo.setUserId(userDo.getRid());
            finUserAccountService.updateOutAccountByUserId(finUserAccountDo);
            FinUserAccountDo userAccountDo=finUserAccountService.getByUserId(userDo.getRid());
            FinUserAccountRecordDo userAccountRecordDo=new FinUserAccountRecordDo();
            userAccountRecordDo.setAmount("-"+bo.accountAmount);
            userAccountRecordDo.setOperator(bo.operator);
            userAccountRecordDo.setRemark(bo.remark);
            userAccountRecordDo.setRefId(String.valueOf(userAccountDo.getRid()));
            userAccountRecordDo.setUserId(userDo.getRid());
            userAccountRecordDo.setType(AccountRecordType.INCASH.getCode());
            userAccountRecordDo.setAccountAmount(String.valueOf(userAccountDo.getTotalAmount()));
            finUserAccountRecordService.saveRecord(userAccountRecordDo);
         }else {
            throw new BizException("第三方请求失败");
        }

    }

    @Override
    public List getBorrowInfoList(FinBorrowBo bo) {
        List borrowInfos=new ArrayList();
        Map data=new HashMap();
        data.put("mobile",bo.mobile);
        for(String key:bo.dataUrl.keySet()){
            String resultStrJsd =FinanceThirdUtil.requestUserBorrowInfosForApi(data,bo.dataUrl.get(key));
            if(StringUtil.isNotBlank(resultStrJsd)) {
                Map respBoJsd= (Map) JSONUtils.parse(resultStrJsd);
                Object borrowData=respBoJsd.get("data");
                if(respBoJsd.get("code").equals(SUCCESS_CODE)&&borrowData!=null){
                    List<Map<String,String>> IsjBorrowInfos= (List<Map<String, String>>) JSONUtils.parse(borrowData.toString());
                    borrowInfos.addAll(IsjBorrowInfos);
                }
            }
        }
        return borrowInfos;
    }

    @Override
    public void pangToAccount(final FinPangBo bo) {
        transactionTemplate.execute(
                new TransactionCallback<Long>() {
                    public Long doInTransaction(TransactionStatus transactionStatus) {
                        FinUserAccountDo userAccountDo=finUserAccountService.getByMobile(bo.mobile);
                        FinUserAccountDo finUserAccountDo=new FinUserAccountDo();
                        finUserAccountDo.setTotalAmount(new BigDecimal(bo.pangAmount));
                        finUserAccountDo.setUserId(userAccountDo.getUserId());
                        finUserAccountService.updateInAccountByUserId(finUserAccountDo);

                        //增加挂账记录
                        FinPangedTradePoolDo pangedTradePoolDo=new FinPangedTradePoolDo();
                        pangedTradePoolDo.setAmount(new BigDecimal(bo.pangAmount));
                        pangedTradePoolDo.setTradeNo(bo.offlineRepayTradeNo);
                        pangedTradePoolDo.setAccountId(String.valueOf(userAccountDo.getRid()));
                        pangedTradePoolDo.setType(InAccountType.PANG.getCode());
                        finPangedTradePoolService.saveRecord(pangedTradePoolDo);

                        FinUserAccountRecordDo userAccountRecordDo=new FinUserAccountRecordDo();
                        userAccountRecordDo.setAmount("+"+new BigDecimal(bo.pangAmount));
                        userAccountRecordDo.setOperator(bo.operator);
                        userAccountRecordDo.setRemark("用户挂账");
                        userAccountRecordDo.setRefId(String.valueOf(userAccountDo.getRid()));
                        userAccountRecordDo.setUserId(userAccountDo.getUserId());
                        userAccountRecordDo.setType(AccountRecordType.PANG.getCode());
                        userAccountRecordDo.setAccountAmount(String.valueOf(userAccountDo.getTotalAmount().add(new BigDecimal(bo.pangAmount))));
                        finUserAccountRecordService.saveRecord(userAccountRecordDo);
                        return 1L;
                    }
                }

        );
    }

    private  Map requestUserForApi(Map data,String host){
        String resultStr =FinanceThirdUtil.syncUserForApi(data,host);
        Map respBo= (Map) JSONUtils.parse(resultStr);
        return respBo;
    }

    @Override
    public FinUserDo getUserCommon(FinUserBo bo) {
        FinUserDo finUserDo = finUserService.getUserByMobile(bo.mobile);
        if (finUserDo == null) {
            Map data = new HashMap();
            data.put("mobile", bo.mobile);
            Map<String, String> userMap = null;
            for(String key:bo.dataUrl.keySet()){
                Map respBoIsj = requestUserForApi(data, bo.dataUrl.get(key));
                if (!respBoIsj.get("code").equals(SUCCESS_CODE)) {
                    continue;
                }else {
                    userMap = (Map) JSONUtils.parse(respBoIsj.get("data").toString());
                }
                if(userMap!=null){
                    FinUserDo userDo = new FinUserDo();
                    userDo.setUserName(userMap.get("userName"));
                    userDo.setMobile(userMap.get("mobile"));
                    userDo.setGender(userMap.get("sex"));
                    userDo.setIdNumber(userMap.get("IdNumber"));
                    userDo.setBirthday(userMap.get("age"));
                    userDo.setGmtModified(new Date());
                    finUserService.saveRecord(userDo);
                    FinUserAccountDo userAccountDo = new FinUserAccountDo();
                    userAccountDo.setUserId(userDo.getRid());
                    userAccountDo.setTotalAmount(BigDecimal.ZERO);
                    userAccountDo.setAmount(BigDecimal.ZERO);
                    userAccountDo.setGmtCreate(new Date());
                    userAccountDo.setGmtModified(new Date());
                    userAccountDo.setTotalRepayamount(BigDecimal.ZERO);
                    finUserAccountService.saveRecord(userAccountDo);
                    return userDo;
                }
            }
            if (userMap == null) {
                throw new BizException("getUserCommon error, can't get userInfo from mobile=" + bo.mobile);
            }

        }
        return finUserDo;
    }

    @Override
    public Map<String, Object> getBorrowDetail(FinBorrowDetailBo bo) {
        FinUserDo finUserDo=finUserService.getUserByMobile(bo.mobile);
        //爱上街
        Map data=new HashMap();
        data.put("borrowNo",bo.borrowNo);
        //爱上街
        String   resultStr =FinanceThirdUtil.requestBorrowDetailForApi(data,bo.dataUrl.get(bo.produce));
        Map respBo= (Map) JSONUtils.parse(resultStr);
        Map<String,Object> borrowInfo=null;
        if(respBo.get("code").equals(SUCCESS_CODE)){
            borrowInfo = (Map<String, Object>) JSONUtils.parse((String) respBo.get("data"));
            borrowInfo.put("userInfo",finUserDo);
            borrowInfo.put("reFundInfos",finRefundService.getRefundListByBorrowNo(bo.borrowNo));
        }
        return borrowInfo;
    }

    @Override
    public List<Map<String, String>> getBorrowInfoByLike(FinBorrowBo bo) {
        List borrowInfos=new ArrayList();
        Map data=new HashMap();
        data.put("mobile",bo.mobile.substring(0,3)+"*****"+bo.mobile.substring(9));
        data.put("realName",bo.realName);
        for(String key:bo.dataUrl.keySet()){
            String resultStrJsd =FinanceThirdUtil.requestUserLikeBorrowInfosForApi(data,bo.dataUrl.get(key));
            if(StringUtil.isNotBlank(resultStrJsd)) {
                Map respBoJsd= (Map) JSONUtils.parse(resultStrJsd);
                if(respBoJsd.get("code").equals(SUCCESS_CODE)){
                    if(respBoJsd.get("data")==null){
                        logger.info("crawler repay fail, getBorrowInfoByLike like search  borrow is null, continue。");
                        continue;
                    }
                    List<Map<String,String>> IsjBorrowInfos= (List<Map<String, String>>) JSONUtils.parse(respBoJsd.get("data").toString());
                    if(IsjBorrowInfos.size()>1){
                        logger.info("crawler repay fail, getBorrowInfoByLike like search more one borrow, borrows="+IsjBorrowInfos);
                        return new ArrayList<>();
                    }
                    borrowInfos.addAll(IsjBorrowInfos);
                }
            }
        }
        return borrowInfos;
    }

}
