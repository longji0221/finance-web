package com.cx.finance.admin.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.req.UserQueryReq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.bo.*;
import com.cx.finance.biz.service.*;
import com.cx.finance.common.ConfigProperties;
import com.cx.finance.common.Constants;
import com.cx.finance.common.enums.*;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.util.*;
import com.cx.finance.dal.domain.*;
import com.cx.finance.dal.query.UserQuery;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;


@Controller
@ResponseBody
@RequestMapping("/api/user")
public class UserController extends BaseController{

    private static final String BORROW_TYPE="self";

    @Resource
    private FinUserService finUserService;

    @Resource
    private FinUserAccountService finUserAccountService;

    @Resource
    private FinUserAccountRecordService finUserAccountRecordService;

    @Resource
    private FinRepaymentOfflineTradePoolService finRepaymentOfflineTradePoolService;


    @Resource
    private FinPangedTradePoolService finPangedTradePoolService;

    @Resource
    private FinThirdApiService finThirdApiService;

    @Resource
    private FinResourceService finResourceService;

    @Resource
    private FinRefundService finRefundService;

    @Resource
    private TransactionTemplate transactionTemplate;



    private List<Map<String,String>> borrows=new ArrayList();


    @RequestMapping(value = "/get.json")
    public Resp<?> getUser(UserQueryReq req, HttpServletRequest request) {
        try {
            return getUserCommon(req.getMobile());
        }catch (Exception e){
            return Resp.fail("请求失败");
        }
    }

    @RequestMapping(value = "/getUserInfo.json")
    public Resp<?> getUserInfo(@RequestBody @Valid UserQueryReq req, HttpServletRequest request) {
        try {
            return getUserCommon(req.getMobile());
        }catch (Exception e){
            return Resp.fail("请求失败");
        }
    }
    @RequestMapping(value = "/getStatus.json")
    public Resp<?> getUserStatus(@RequestBody @Valid UserQueryReq req, HttpServletRequest request) {
        try {
            return Resp.succ();
        }catch (Exception e){
            return Resp.fail("请求失败");
        }
    }

    @RequestMapping(value = "/tradeNo.json")
    public Resp<?> getTradeNos(UserQueryReq req,HttpServletRequest request) {
        String mobile = request.getParameter("mobile");
        try {
            List<HashMap> dataList= finRepaymentOfflineTradePoolService.getNoUseTradesByMobile(mobile);
            List<HashMap> tradesList= new ArrayList<>();
            for(HashMap map:dataList){
                HashMap trade=new HashMap();
                trade.put("tradeNo",(map.get("amount").toString()+"元:"+map.get("tradeNo")));
                tradesList.add(trade);
            }
            return Resp.succ(tradesList,"");
        }catch (Exception e){
            return Resp.fail("请求失败");
        }
    }
    @RequestMapping(value = "/refunded.json")
    public Resp<?> refunded(HttpServletRequest request) {
        String borrowNo = request.getParameter("borrowNo");
        try {
            List<FinRefundDo> finRefundDos= finRefundService.getRefundListNoFailByBorrowNo(borrowNo);
            BigDecimal refundedAmount=BigDecimal.ZERO;
            for(FinRefundDo refund:finRefundDos){
                refundedAmount=refundedAmount.add(refund.getAmount());
            }
            return Resp.succ(refundedAmount,"");
        }catch (Exception e){
            return Resp.fail("请求失败");
        }
    }

    @RequestMapping(value = "/getBorrowDetail.json")
    public Resp<?> getBorrowInfo(UserQueryReq req, HttpServletRequest request) {
        FinBorrowDetailBo bo=new FinBorrowDetailBo();
        try {
            bo.mobile=req.getMobile();
            bo.borrowNo=req.getBorrowNo();
            bo.produce=req.getProduce();
            bo.dataUrl=finResourceService.getApiUrl();
            Map<String,Object> borrowInfo=finThirdApiService.getBorrowDetail(bo);
            return Resp.succ(borrowInfo,"");
        }catch (Exception e){
            logger.info("查看案件："+e+"-Bo=[{}]:"+ bo.mobile+bo.borrowNo+bo.produce);
            return Resp.fail(e.getMessage()==null?"请求失败":e.getMessage());
        }
    }

    @RequestMapping(value = "/getAccount.json")
    public Resp<?> getUserAccount(UserQueryReq req,HttpServletRequest request) {
       try {
           if(finUserService.getUserByMobile(req.getMobile())==null){
               return Resp.fail("用户为空");
           }
           List<HashMap>  userAccountDo=null;
           if(StringUtil.isNotBlank(req.getMobile())){
               userAccountDo= finRepaymentOfflineTradePoolService.getNoUseTradesByMobile(req.getMobile());
           }
           BigDecimal amountSum=BigDecimal.ZERO;
           if(userAccountDo.size()!=0){
               Iterator iteratorAccount=userAccountDo.iterator();
               while (iteratorAccount.hasNext()){
                   HashMap<String,BigDecimal> map= (HashMap) iteratorAccount.next();
                   HashMap data=new HashMap();
                   data.put("tradeNo",map.get("tradeNo"));
                   if(!CollectionUtil.requestCollectionOfflineTradeNo(data)){
                       FinPangedTradePoolDo tradePoolDo=new FinPangedTradePoolDo();
                       tradePoolDo.setType(InAccountType.COLLECTION_REPAY.getCode());
                       tradePoolDo.setAccountId(finUserAccountService.getByMobile(req.getMobile()).getRid().toString());
                       tradePoolDo.setTradeNo(String.valueOf(map.get("tradeNo")));
                       tradePoolDo.setAmount(map.get("amount"));
                       finPangedTradePoolService.saveRecord(tradePoolDo);
                       continue;
                   }
                   amountSum=amountSum.add(map.get("amount")) ;
               }
           }
           Map map=new HashMap();
           map.put("amount",amountSum.toString());
           FinBorrowBo bo=new FinBorrowBo();
           bo.mobile=req.getMobile();
           bo.dataUrl=finResourceService.getApiUrl();
           List borrowInfos= finThirdApiService.getBorrowInfoList(bo);
           if(borrowInfos.size()==0&&userAccountDo.size()==0){
               return Resp.fail("借款和账户为空");
           }
           //清空并重新赋值
           borrows.clear();
           borrows.addAll(borrowInfos);
           int noFinishSelfBorrow=0;
           int noFinishThirdBorrow=0;
           BigDecimal selfReceivableAmount=BigDecimal.ZERO;
           BigDecimal thirdReceivableAmount=BigDecimal.ZERO;

           BigDecimal selfRepayAmount=BigDecimal.ZERO;
           BigDecimal thirdRepayAmount=BigDecimal.ZERO;

           BigDecimal selfResAmount=BigDecimal.ZERO;
           BigDecimal thirdResAmount=BigDecimal.ZERO;

           Iterator iterator=borrowInfos.iterator();
           while (iterator.hasNext()){
               Map borrow= (Map) iterator.next();
               if(!"FINISHED".equals(borrow.get("status"))){
                   if(splitBorrow(borrow,BORROW_TYPE)!=null){
                       noFinishSelfBorrow=noFinishSelfBorrow+1;
                       selfReceivableAmount= selfReceivableAmount.add(new BigDecimal(String.valueOf(borrow.get("sumAmount"))));
                       selfRepayAmount=selfRepayAmount.add(new BigDecimal(String.valueOf(borrow.get("repayAmount"))));
                       selfResAmount=selfResAmount.add(new BigDecimal(String.valueOf(borrow.get("restAmount"))));
                   }else {
                       noFinishThirdBorrow=noFinishThirdBorrow+1;
                       thirdReceivableAmount= thirdReceivableAmount.add(new BigDecimal(String.valueOf(borrow.get("sumAmount"))));
                       thirdRepayAmount=thirdRepayAmount.add(new BigDecimal(String.valueOf(borrow.get("repayAmount"))));
                       thirdResAmount=thirdResAmount.add(new BigDecimal(String.valueOf(borrow.get("restAmount"))));
                   }
               }
           }
           map.put("selfReceivableAmount",selfReceivableAmount);
           map.put("thirdReceivableAmount",thirdReceivableAmount);
           map.put("selfRepayAmount",selfRepayAmount);
           map.put("thirdRepayAmount",thirdRepayAmount);
           map.put("selfResAmount",selfResAmount);
           map.put("thirdResAmount",thirdResAmount);
           map.put("noFinishSelfCount",noFinishSelfBorrow);
           map.put("noFinishThirdCount",noFinishThirdBorrow);
           return Resp.succ(map,"");
       }catch (Exception e){
           logger.info("getAccount is fail：case=",e);
           return Resp.fail(e.getMessage()==null?"请求失败":e.getMessage());
       }
    }


    @RequestMapping(value = "/listBorrow.json")
    public Resp<?> getUserBorrows(@RequestBody @Valid UserQueryReq req, HttpServletRequest request) {
        try {
            UserQuery dalQuery = req.toDalQuery();
            if(StringUtil.isBlank(req.getMobile())){
                return Resp.fail("手机号为空");
            }
            List<Map<String,String>> splitedBorrows= splitBorrows(borrows,req.getAscription());
            return Resp.succ(QueryRespData.gen(splitedBorrows, dalQuery.getTotalCount()),"");
        }catch (Exception e){
            logger.info("listBorrow is fail：case=",e);
            return Resp.fail(e.getMessage()==null?"请求失败":e.getMessage());
        }
    }

    List<Map<String,String>> splitBorrows(List<Map<String,String>> borrows,String ascription){
        FinResourceDo resource=finResourceService.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,BORROW_TYPE.equals(ascription)?Constants.THIRD_SELF_PRODUCT:Constants.THIRD_OTHER_PRODUCT).get(0);
        List splitedBorrows=new ArrayList();
        for(Map<String,String> borrow:borrows){
            if(resource.getValue().contains(borrow.get("companyId"))){
                splitedBorrows.add(borrow);
            }
        }
        return splitedBorrows;
    }


    Map<String,String> splitBorrow(Map<String,String> borrow,String ascription){
        FinResourceDo resource=finResourceService.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,BORROW_TYPE.equals(ascription)?Constants.THIRD_SELF_PRODUCT:Constants.THIRD_OTHER_PRODUCT).get(0);
        if(resource.getValue().contains(borrow.get("companyId"))){
            return borrow;
        }
        return null;
    }



    @RequestMapping(value = "/listAccountRecord.json")
    public Resp<?> accountRecord(@RequestBody @Valid UserQueryReq req, HttpServletRequest request) {
        try {
            if(StringUtil.isBlank(req.getMobile())){
                return Resp.succ(null,"");
            }
            List<HashMap> mapList= finRepaymentOfflineTradePoolService.getNoUseTradesByMobile(req.getMobile());
            List<HashMap> recordList=new ArrayList<>();
            for(HashMap map:mapList){
                HashMap record=new HashMap();
                record.put("tradeNo",map.get("tradeNo"));
                record.put("amount",map.get("amount"));
                record.put("rid",map.get("rid"));
                record.put("type",map.get("type"));
                record.put("operator",map.get("operator"));
                record.put("gmtCreate",map.get("gmtCreate"));
                recordList.add(record);
            }
            return Resp.succ(recordList,"");
        }catch (Exception e){
            logger.info("listAccountRecord is fail：case=",e);
            return Resp.fail("请求失败");
        }
    }


    @RequestMapping(value = "/refund.json")
    public Resp<?> reFund(UserQueryReq req, HttpServletRequest request){
        try {
            if(new BigDecimal(req.getUserAmount()).compareTo(new BigDecimal(req.getReFundAmount()))<0){
                return Resp.fail("可退金额不足！");
            }
            if(BigDecimal.ZERO.compareTo(new BigDecimal(req.getReFundAmount()))==0){
                return Resp.fail("退款金额不能为0！");
            }
            if(BigDecimal.ZERO.compareTo(new BigDecimal(req.getReFundAmount()))>0){
                return Resp.fail("退款金额不能小余0！");
            }
            if(!StringUtil.isAllNotEmpty(req.getReFundAmount(),req.getType())){
                return  Resp.fail("参数错误");
            }
            FinRefundBo bo=new FinRefundBo();
            bo.mobile=req.getMobile();
            bo.realName=Sessions.getRealname(request);
            bo.reFundAmount=req.getReFundAmount();
            bo.remark=req.getRemark();
            bo.type=req.getType();
            bo.borrowNo=req.getBorrowNo();
            bo.userAccount=req.getUserAccount();
            finRefundService.executeRefund(bo);
            return  Resp.succ();
        }catch (Exception e){
            logger.info("refund is fail：case=",e);
            return Resp.fail("退款失败");
        }
    }

    private Resp<?> getUserCommon(String mobile){
        try {
            if(StringUtil.isBlank(mobile)){
                return Resp.succ();
            }
            FinUserBo bo=new FinUserBo();
            bo.mobile=mobile;
            bo.dataUrl=finResourceService.getApiUrl();
            FinUserDo finUserDo=finThirdApiService.getUserCommon(bo);
            List list=new ArrayList();
            list.add(finUserDo);
            return Resp.succ(list,"");
        }catch (BizException e){
            return Resp.fail("数据为空");
        }catch (Exception e){
            logger.info("getUserCommon is fail：case=",e);
            return Resp.fail("请求异常");
        }
    }


    private  Map requestUserForApi(Map data,String host){
        String resultStr =FinanceThirdUtil.syncUserForApi(data,host);
        Map respBo= (Map) JSONUtils.parse(resultStr);
        return respBo;
    }



}
