package com.cx.finance.admin.web.controller.third;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.*;
import com.cx.finance.common.Constants;
import com.cx.finance.common.util.CollectionUtil;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import com.cx.finance.dal.domain.FinResourceDo;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cx.finance.admin.spring.NotNeedLogin;
import com.cx.finance.admin.web.controller.BaseController;
import com.cx.finance.biz.bo.CrawlerAlipayBo;
import com.cx.finance.biz.bo.FinBorrowBo;
import com.cx.finance.biz.bo.FinOfflineRepayBo;
import com.cx.finance.biz.bo.FinPangBo;
import com.cx.finance.biz.bo.FinUserBo;
import com.cx.finance.common.enums.BorrowStatus;
import com.cx.finance.common.enums.FinOfflineRepayType;
import com.cx.finance.common.enums.OfflineRepayType;
import com.cx.finance.common.enums.YesNoStatus;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.NumberUtil;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.domain.FinRepaymentOfflineTradePoolDo;
import com.cx.finance.dal.domain.FinUserDo;

@NotNeedLogin
@RestController
@RequestMapping("/third/crawler")
public class CrawlerController extends BaseController{
	@Resource
	private FinRepaymentOfflineTradePoolService finRepaymentOfflineTradePoolService;
	
	@Resource
    private FinThirdApiService finThirdApiService;
	@Resource
	private FinResourceService finResourceService;
	@Resource
	private FinOfflieRepayService finOfflieRepayService;

	@Resource
	private FinPangedTradePoolService finPangedTradePoolService;

	private static final String RESP_SUCCESS = "SUCCESS";
	private static final String RESP_FAIL = "FAIL";


	/**
     * 接收支付包爬虫爬取的账单数据
     */
    @RequestMapping(value = "/render.up")
    @ResponseBody
    public String render(@RequestBody MultiValueMap<String, String> params, HttpServletRequest request) {
        try {
			String alipaylist = params.getFirst("alipaylist");
        	String sysOperator = "AlipayCrawler";
        	
        	if(StringUtils.isBlank(alipaylist)) {
        		logger.warn("render, alipayList is null!");
        		return RESP_SUCCESS;
        	}
        	
        	JSONArray aplipaylist = JSONObject.parseArray(alipaylist);
            final List<CrawlerAlipayBo> dataList = parseAlipayData(aplipaylist);
        	
            new Thread(){ public void run() {
            	for(CrawlerAlipayBo alipayBo : dataList) {
            		dealPer(alipayBo, sysOperator);
            	}
            }}.start();
            return RESP_SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESP_FAIL;
        }
    }
    
    private void dealPer(CrawlerAlipayBo alipayBo, String sysOpterator) {

		logger.warn("dealPer log start, alipayBo remark[" + alipayBo.remark + "]");


		FinRepaymentOfflineTradePoolDo poolDo;
    	String tradeNo = alipayBo.orderNo;
    	String mobile = alipayBo.mobile;

		FinPangedTradePoolDo finPangedTradePoolDo=finPangedTradePoolService.getPangedTradePoolDataByTradeNo(tradeNo);
		if(finPangedTradePoolDo!=null){
			logger.warn("dealPer warn, tar tradeNo [" + tradeNo + "] is repeat, ignore");
			return;
		}
		HashMap data=new HashMap();
		data.put("tradeNo",tradeNo);
		if(!CollectionUtil.requestCollectionOfflineTradeNo(data)){
			return;
		}


		if(mobile.contains("@")) { // 手机号为邮箱
			logger.warn("dealPer warn, tar tradeNo [" + tradeNo + "] has invalid mobile [" + mobile + "], ignore");
			return;
		}


		try {
			poolDo = FinRepaymentOfflineTradePoolDo.build(alipayBo.benefitAccount, mobile, FinOfflineRepayType.ALIPAY,
					tradeNo, new BigDecimal(alipayBo.money), alipayBo.remark, alipayBo.realName, DateUtil.parseDate(alipayBo.createTime, "yyyy-MM-dd HH:mm:ss"),"CrawlerAlipay" );
			finRepaymentOfflineTradePoolService.saveRecord(poolDo);
			logger.warn("dealPer log, tar tradeNo [" + tradeNo + "] save to pool,operator system crawler ");
		} catch (Exception e) { // 流水号已入库
			logger.error("dealPer error, tar tradeNo  [" + tradeNo + "] repeat possbile, db error! msg = " + e.getMessage(), e);
			return;
		}




    	try {
    		Map<String, String> dataUrl = finResourceService.getApiUrl();
    		
    		FinUserBo bo=new FinUserBo();
            bo.mobile = mobile;
            bo.dataUrl = dataUrl;
            if(mobile.contains("*")){
				FinBorrowBo borrowBo = new FinBorrowBo();
				borrowBo.mobile = mobile;
				borrowBo.dataUrl = finResourceService.getApiUrl();
				borrowBo.realName=alipayBo.realName;
				List<Map<String,String>> borrows=finThirdApiService.getBorrowInfoByLike(borrowBo);
				if(borrows!=null&&borrows.size()==1){
					Map<String,String > borrowInfo=borrows.get(0);
					BigDecimal repayAmount = new BigDecimal(alipayBo.money); //单位元
					BigDecimal restAmount = NumberUtil.objToBigDecimalDefault(borrowInfo.get("restAmount"), BigDecimal.ZERO); //单位元
					Date cur = new Date();
					FinOfflineRepayBo offlineRepayBo =new FinOfflineRepayBo();
					offlineRepayBo.borrowNo=borrowInfo.get("orderNo");
					offlineRepayBo.companyId = borrowInfo.get("companyId");
					offlineRepayBo.mobile=mobile;
					offlineRepayBo.isAllFinish= YesNoStatus.NO.getCode();
					offlineRepayBo.operator = sysOpterator;
					offlineRepayBo.payTime = DateUtil.formatDate(cur, DateUtil.DATE_TIME_SHORT);
					offlineRepayBo.repayAmount = repayAmount;
					offlineRepayBo.repayTradeNo = tradeNo;
					offlineRepayBo.restAmount = restAmount;
					offlineRepayBo.remark = "爬虫模糊还款";
					offlineRepayBo.repayCardNum = "";
					offlineRepayBo.repayType = OfflineRepayType.ALIPAY.code;
					offlineRepayBo.dataUrl = finResourceService.getApiUrl();
					finOfflieRepayService.offlineRepay(offlineRepayBo);
					logger.warn("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] mobile and name * like search  repay borrow, mobile [" + offlineRepayBo.mobile+ "]， return ");
					return;
				}else {
					logger.warn("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] like search  have more a borrow or no borrow ， return ");
					return;
				}
			}
			FinUserDo  userCommon = finThirdApiService.getUserCommon(bo);
			if(userCommon == null) {	//无用户
    			logger.warn("dealPer warn, tar tradeNo [" + tradeNo + "] can't find refer user, ignore");
    			return;
    		}
    		
    		FinBorrowBo borrowBo = new FinBorrowBo();
    		borrowBo.mobile = mobile;
    		borrowBo.dataUrl = finResourceService.getApiUrl();
            List<Map<String,String>> borrowInfos = finThirdApiService.getBorrowInfoList(borrowBo);
            

            if(borrowInfos == null || borrowInfos.size() == 0) { //无借款
                logger.info("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] have no borrows, return ");
                return;
            }
            //是否备注指定还款到产品
			String appointRemarkProduct="";
			List<FinResourceDo> resourceDos=finResourceService.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,Constants.THIRD_PRODUCT);
			if(StringUtil.isNotBlank(alipayBo.remark)){
				for(FinResourceDo finResourceDo:resourceDos) {
					if (alipayBo.remark.contains(finResourceDo.getValue1())) {
						appointRemarkProduct=finResourceDo.getName();
					}
				}
			}
            Map<String,String> validBorrowInfo = null;
            List<Map<String,String>> validBorrowInfoList=new ArrayList<>();
            //有配置指定账户入还指定产品借款
			FinResourceDo resourceDo=finResourceService.getFinResourceListByTypeAndSecTypeAndValue(Constants.FINANCE_CONFIG,Constants.REPAY_ACCOUNT,alipayBo.accountName.trim());
			for(Map<String,String> borrowInfo : borrowInfos) {
            	if(BorrowStatus.TRANSFERRED.name().equals(borrowInfo.get("status"))) {
					BigDecimal repayAmount = new BigDecimal(alipayBo.money); //单位元
					BigDecimal restAmount = NumberUtil.objToBigDecimalDefault(borrowInfo.get("restAmount"), BigDecimal.ZERO); //单位元
					Date cur = new Date();
					FinOfflineRepayBo offlineRepayBo =new FinOfflineRepayBo();
					offlineRepayBo.borrowNo=borrowInfo.get("orderNo");
					offlineRepayBo.mobile=mobile;
					offlineRepayBo.isAllFinish= YesNoStatus.NO.getCode();
					offlineRepayBo.operator = sysOpterator;
					offlineRepayBo.payTime = DateUtil.formatDate(cur, DateUtil.DATE_TIME_SHORT);
					offlineRepayBo.repayAmount = repayAmount;
					offlineRepayBo.repayTradeNo = tradeNo;
					offlineRepayBo.restAmount = restAmount;
					offlineRepayBo.remark = "爬虫还款";
					offlineRepayBo.repayCardNum = "";
					offlineRepayBo.repayType = OfflineRepayType.ALIPAY.code;
					offlineRepayBo.dataUrl = finResourceService.getApiUrl();
            		//有指定的入账账户
					if(resourceDo!=null && resourceDo.getName().equals(borrowInfo.get("companyId"))){
						offlineRepayBo.companyId = resourceDo.getName();
						finOfflieRepayService.offlineRepay(offlineRepayBo);
						logger.info("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] have a appoint account ["+alipayBo.accountName+"] offline repay");
						return;
					}
					if(StringUtils.isNotBlank(appointRemarkProduct)&&borrowInfo.get("companyId").equals(appointRemarkProduct)){
						offlineRepayBo.companyId = appointRemarkProduct;
						finOfflieRepayService.offlineRepay(offlineRepayBo);
						logger.info("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] like remark ["+alipayBo.remark+"] offline repay");
						return;
					}
					validBorrowInfoList.add(borrowInfo);
            	}
            }
            if(validBorrowInfoList.size()==1) { // 有唯一一笔借款待还
				validBorrowInfo=validBorrowInfoList.get(0);
				BigDecimal repayAmount = new BigDecimal(alipayBo.money); //单位元
				BigDecimal restAmount = NumberUtil.objToBigDecimalDefault(validBorrowInfo.get("restAmount"), BigDecimal.ZERO); //单位元
				Date cur = new Date();

				FinOfflineRepayBo offlineRepayBo =new FinOfflineRepayBo();
				offlineRepayBo.borrowNo=validBorrowInfo.get("orderNo");
				offlineRepayBo.companyId = validBorrowInfo.get("companyId");
				offlineRepayBo.mobile=mobile;
				offlineRepayBo.isAllFinish= YesNoStatus.NO.getCode();
				offlineRepayBo.operator = sysOpterator;
				offlineRepayBo.payTime = DateUtil.formatDate(cur, DateUtil.DATE_TIME_SHORT);
				offlineRepayBo.repayAmount = repayAmount;
				offlineRepayBo.repayTradeNo = tradeNo;
				offlineRepayBo.restAmount = restAmount;
				offlineRepayBo.remark = "爬虫还款";
				offlineRepayBo.repayCardNum = "";
				offlineRepayBo.repayType = OfflineRepayType.ALIPAY.code;
				offlineRepayBo.dataUrl = finResourceService.getApiUrl();
				finOfflieRepayService.offlineRepay(offlineRepayBo);
				logger.info("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] only a no finish borrow  offline repay");
			}else {
				// 有多笔待还款借款
				logger.warn("dealPer done, tar tradeNo [" + alipayBo.orderNo + "] have more than one borrows = " + JSON.toJSONString(borrowInfos) + ", return ");
            }
            
		} catch (Exception e) {
			logger.error("dealPer error, msg = " + e.getMessage() + ", tar alipayBo = " + JSON.toJSONString(alipayBo), e);
		}
    }
    private List<CrawlerAlipayBo> parseAlipayData(JSONArray aplipaylist) throws UnsupportedEncodingException {
        List<CrawlerAlipayBo> list = new ArrayList<CrawlerAlipayBo>();
        
        String benefitAccount, mobile, orderNo, createTime, realName, money, remark, remarkAccount;
        for (int i=0; i<aplipaylist.size(); i++){
            JSONObject payData = (JSONObject)aplipaylist.get(i);
            mobile =  payData.getString("aliAccount");
            if (mobile == null){
            	logger.warn("parseAlipayData, mobile is null, tar alipayBo = " + payData.toJSONString());
                continue;
            }
            
            String mobileFromRemarkAccount = StringUtil.extractMobile(payData.getString("remarkAccount"));
            if(StringUtil.isBlank(mobileFromRemarkAccount)) { mobileFromRemarkAccount = StringUtil.extractMobile(payData.getString("remark")); }
            if(StringUtil.isNotBlank(mobileFromRemarkAccount)) { mobile = mobileFromRemarkAccount; }
            
            benefitAccount = payData.getString("benefitAccount");
			String accountName=benefitAccount;
            benefitAccount = finResourceService.getCompanySimpleNameByBenifitAccount(benefitAccount);
            orderNo =  payData.getString("orderNo");
            createTime =  payData.getString("createTime");
            realName =  payData.getString("userName") ;
            money =  payData.getString("money");
            remark = payData.getString("remark") ;
            remarkAccount = payData.getString("remarkAccount");
            
            list.add(CrawlerAlipayBo.gen(benefitAccount, mobile, orderNo, createTime, realName, money, remark, remarkAccount,accountName));
        }
        return list;
    }


}
