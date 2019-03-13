package com.cx.finance.admin.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.req.InAccountQueryReq;
import com.cx.finance.admin.web.dto.req.UserQueryReq;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.bo.FinOfflineRepayBo;
import com.cx.finance.biz.service.*;
import com.cx.finance.common.ConfigProperties;
import com.cx.finance.common.Constants;
import com.cx.finance.common.enums.AccountRecordType;
import com.cx.finance.common.enums.CompanyType;
import com.cx.finance.common.enums.InAccountType;
import com.cx.finance.common.enums.OfflineRepayType;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.util.*;
import com.cx.finance.dal.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;


@Controller
@ResponseBody
@RequestMapping("/api/offline/")
public class OfflineRepayController extends BaseController{



    @Resource
    private FinRepaymentOfflineTradePoolService finRepaymentOfflineTradePoolService;

    @Resource
    private FinUserAccountService finUserAccountService;

    @Resource
    private FinUserService finUserService;

    @Resource
    private FinUserAccountRecordService finUserAccountRecordService;

    @Resource
    private FinPangedTradePoolService finPangedTradePoolService;

    @Resource
    private FinOfflieRepayService finOfflieRepayService;

    @Resource
    private FinResourceService finResourceService;

    @RequestMapping(value = "/repay.json")
    public Resp<?> getUser(HttpServletRequest request) {
        try {
            String repayType = request.getParameter("repayType");
            String borrowNo = request.getParameter("borrowNo");
            String repayTradeNo = request.getParameter("repayTradeNo").trim();
            BigDecimal repayAmount = NumberUtil.objToBigDecimalDefault(request.getParameter("repayAmount"), BigDecimal.ZERO); //单位元
            BigDecimal restAmount = NumberUtil.objToBigDecimalDefault(request.getParameter("restAmount"), BigDecimal.ZERO); //单位元
            String repayCardNum = request.getParameter("bankCard");
            String payTime = request.getParameter("payTime");
            String mobile = request.getParameter("mobile");
            String remark = request.getParameter("remark");
            String companyId = request.getParameter("companyId");
            String isAllFinish = request.getParameter("isAllFinish");
            String resultStr =null;

            if(StringUtil.isBlank(repayTradeNo)){
                return Resp.fail("流水号不能为空");
            }
            if(OfflineRepayType.BANKCARD.code.equals(repayType) && StringUtil.isBlank(repayCardNum)){
                return Resp.fail("银行卡不能为空");
            }
            FinPangedTradePoolDo finPangedTradePoolDo=finPangedTradePoolService.getPangedTradePoolDataByTradeNo(repayTradeNo);
            logger.info("offline repay tradeNo find finPangedTradePoolDo is"+finPangedTradePoolDo);
            if(finPangedTradePoolDo!=null){
                return Resp.fail("流水号已使用入账,不能重复使用。");
            }
            FinRepaymentOfflineTradePoolDo poolDo=null;
            if(OfflineRepayType.ALIPAY.code.equals(repayType) && StringUtil.isNotBlank(repayTradeNo)){
                poolDo= finRepaymentOfflineTradePoolService.getRepaymentOfflinePoolDataByTradeNo(repayTradeNo);
                if(poolDo==null){
                    return Resp.fail("流水号有误，请核实（温馨提示：如用户确认已还款仍入账失败，请找财务核实）");
                }
                if(repayAmount.setScale(2, BigDecimal.ROUND_DOWN).compareTo(poolDo.getAmount().setScale(2,BigDecimal.ROUND_DOWN))!=0){
                    return Resp.fail("请确认输入金额与流水号无误后再进行尝试（温馨提示：如用户确认已还款仍入账失败，请找财务核实）");
                }
                if(poolDo.getPayTime()==null){
                    return Resp.fail("线下还款时间为空（请联系财务确定入账数据完整性）。");
                }
            }
            HashMap data=new HashMap();
            data.put("tradeNo",repayTradeNo);
            if(!CollectionUtil.requestCollectionOfflineTradeNo(data)){
                FinPangedTradePoolDo tradePoolDo=new FinPangedTradePoolDo();
                tradePoolDo.setType(InAccountType.COLLECTION_REPAY.getCode());
                tradePoolDo.setAccountId(finUserAccountService.getByMobile(mobile).getRid().toString());
                tradePoolDo.setTradeNo(repayTradeNo);
                tradePoolDo.setAmount(poolDo.getAmount());
                finPangedTradePoolService.saveRecord(tradePoolDo);
                return Resp.fail("流水号已在催收还款。");
            }
            if(poolDo!=null){
                payTime=DateUtil.formatDate(poolDo.getPayTime(),"yyyy-MM-dd 23:59:58");
            }else {
                payTime=DateUtil.formatDate(DateUtil.parseDate(payTime),"yyyy-MM-dd 23:59:58");
            }
            FinOfflineRepayBo bo=new FinOfflineRepayBo();
            bo.borrowNo=borrowNo;
            bo.companyId=companyId;
            bo.mobile=mobile;
            bo.isAllFinish=isAllFinish;
            bo.operator=Sessions.getRealname(request);
            bo.payTime=payTime;
            bo.repayAmount=repayAmount;
            bo.repayTradeNo=repayTradeNo;
            bo.restAmount=restAmount;
            bo.remark=remark;
            bo.repayCardNum=repayCardNum;
            bo.repayType=repayType;
            bo.dataUrl=finResourceService.getApiUrl();
            finOfflieRepayService.offlineRepay(bo);
            return Resp.succ();
        }catch(BizException e){
            return Resp.fail(e.getMsg());
        }catch(Exception e){
            logger.info("线下还款异常：",e);
            return Resp.fail("请求失败");
        }
    }

    @RequestMapping(value = "/inAccount.json")
    public Resp<?> inAccount(InAccountQueryReq req, HttpServletRequest request) {
        try {
            if("请选择入帐金额".equals(req.getInAmountTradeNo())){
                return Resp.fail("入账金额不能为空！");
            }
            String[] amountAndTradNo=req.getInAmountTradeNo().split("元:");
            String borrowNo = request.getParameter("borrowNo");
            String repayTradeNo = amountAndTradNo[1];
            BigDecimal repayAmount = NumberUtil.objToBigDecimalDefault(amountAndTradNo[0], BigDecimal.ZERO); //单位元
            String payTime = request.getParameter("payTime");
            String mobile = request.getParameter("mobile");
            String remark = request.getParameter("remark");
            String companyId = request.getParameter("companyId");
            String isAllFinish = request.getParameter("isAllFinish");
            FinPangedTradePoolDo finPangedTradePoolDo=finPangedTradePoolService.getPangedTradePoolDataByTradeNo(repayTradeNo);
            if(finPangedTradePoolDo!=null){
                return Resp.fail("流水号已使用入账,不能重复使用。");
            }
            FinRepaymentOfflineTradePoolDo poolDo= finRepaymentOfflineTradePoolService.getRepaymentOfflinePoolDataByTradeNo(repayTradeNo);
            if(poolDo==null){
                return Resp.fail("流水号有误，请核实（温馨提示：如用户确认已还款仍入账失败，请找财务核实）");
            }
            if(repayAmount.setScale(2, BigDecimal.ROUND_DOWN).compareTo(poolDo.getAmount().setScale(2,BigDecimal.ROUND_DOWN))!=0){
                return Resp.fail("请确认输入金额与流水号无误后再进行尝试（温馨提示：如用户确认已还款仍入账失败，请找财务核实）");
            }
            HashMap data=new HashMap();
            data.put("tradeNo",repayTradeNo);
            if(!CollectionUtil.requestCollectionOfflineTradeNo(data)){
                FinPangedTradePoolDo tradePoolDo=new FinPangedTradePoolDo();
                tradePoolDo.setType(InAccountType.COLLECTION_REPAY.getCode());
                tradePoolDo.setAccountId(finUserAccountService.getByMobile(mobile).getRid().toString());
                tradePoolDo.setTradeNo(repayTradeNo);
                tradePoolDo.setAmount(poolDo.getAmount());
                finPangedTradePoolService.saveRecord(tradePoolDo);
                return Resp.fail("流水号已在催收还款。");
            }
            if(poolDo!=null){
                payTime=DateUtil.formatDate(poolDo.getPayTime(),"yyyy-MM-dd 23:59:58");
            }else {
                payTime=DateUtil.formatDate(DateUtil.parseDate(payTime),"yyyy-MM-dd 23:59:58");
            }
            FinOfflineRepayBo bo=new FinOfflineRepayBo();
            bo.borrowNo=borrowNo;
            bo.companyId=companyId;
            bo.mobile=mobile;
            bo.isAllFinish=isAllFinish;
            bo.operator=Sessions.getRealname(request);
            bo.payTime=payTime;
            bo.repayAmount=repayAmount;
            bo.repayTradeNo=repayTradeNo;
            bo.remark=remark;
            bo.repayType=OfflineRepayType.ALIPAY.code;
            bo.dataUrl=finResourceService.getApiUrl();
            finOfflieRepayService.offlineRepay(bo);
            return Resp.succ();
        }catch (BizException e){
            return Resp.fail(e.getMsg());
        }catch (Exception e){
            logger.info("inAccount is fail：case=",e);
            return Resp.fail("请求失败");
        }
    }

   private void checkTradeNo(String tradeNo){

   }
}
