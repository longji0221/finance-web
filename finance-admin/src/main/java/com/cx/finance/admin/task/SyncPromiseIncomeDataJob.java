package com.cx.finance.admin.task;

import java.util.Date;
import java.util.List;

import com.cx.finance.common.ConfigProperties;
import com.cx.finance.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cx.finance.biz.bo.FinanceSystemDataBo;
import com.cx.finance.biz.service.FinPromiseIncomeService;
import com.cx.finance.common.Constants;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.DigestUtil;
import com.cx.finance.common.util.HttpUtil;
import com.cx.finance.dal.domain.FinPromiseIncomeDo;
import org.springframework.transaction.annotation.Transactional;

/**
     *
     * 同步各业务应收数据
     *
     * */

    @Component
    public class SyncPromiseIncomeDataJob {

        @Autowired
        FinPromiseIncomeService finPromiseIncomeService;

        Logger logger = LoggerFactory.getLogger(com.cx.finance.admin.task.SyncPromiseIncomeDataJob.class);

        private static String urlJsd = null;
        private static String urlDsed = null;

        private static String getJsdUrl() {
            if (urlJsd == null) {
                urlJsd = ConfigProperties.get(Constants.JSD_HOST);
                return urlJsd;
            }
            return urlJsd;
        }
        private static String getDsedUrl() {
            if (urlDsed == null) {
                urlDsed = ConfigProperties.get(Constants.DSED_HOST);
                return urlDsed;
            }
            return urlDsed;
        }

        private static String dataType="PROMISE_INCOME";

        @Scheduled(cron = "1 20 02 * * ?")
        public void run() throws InterruptedException {
            try{
              //  dealJSDData();  //处理极速贷应收数据
            } catch (Exception e){
                logger.error("SyncPromiseIncomeDataJob dealJSDData error, case=",e);
            }
                Thread.sleep(1000);
            try{
             //   dealDSEDData();//处理极速贷应收数据
            } catch (Exception e){
                logger.error("SyncPromiseIncomeDataJob dealDSEDData error, case=",e);
            }
            logger.info("SyncPromiseIncomeDataJob start,time=" + new Date());
            logger.info("SyncPromiseIncomeDataJob end,time=" + new Date());
        }
        @Transactional
        public void dealJSDData() {
            Date currDate = new Date();
            FinanceSystemDataBo data = new FinanceSystemDataBo();
            String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
            data.setTimestamp(timestamp);
            data.setDataType(dataType);
            String sign= Constants.JSDSING+timestamp;
            data.setSign(DigestUtil.MD5(sign));
            String reqResult = HttpUtil.doHttpPostByJsonString(getJsdUrl() + "/third/finance/getData", toJSONString(data),5000,5000);
            if(StringUtil.isBlank(reqResult)){
                logger.info("dealJSDData finPromiseIncomeService。reqResult is NULL");
                return ;
            }
            JSONObject paramsJson = JSON.parseObject(reqResult);
            String code = paramsJson.getString("code");
            if("200".equals(code)){
                String datas = paramsJson.getString("data");
                List<FinPromiseIncomeDo> list = JSONArray.parseArray(datas,FinPromiseIncomeDo.class);

                if(list.size()>0 && finPromiseIncomeService.batchinsert(list)>0){
                    logger.info("dealJSDData finPromiseIncomeService。batchInsert is success");
                } else {
                    logger.info("dealJSDData finPromiseIncomeService。batchInsert is failed or list.size<0");
                }
            } else {
                logger.info("response code is not 200");
            }
        }



        @Transactional
        public void dealDSEDData(){
            Date currDate = new Date();
            FinanceSystemDataBo data = new FinanceSystemDataBo();
            String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
            data.setTimestamp(timestamp);
            data.setDataType(dataType);
            String sign= Constants.EDSSING+timestamp;
            data.setSign(DigestUtil.MD5(sign));
            String reqResult = HttpUtil.doHttpPostByJsonString(getDsedUrl() + "/third/finance/getData", toJSONString(data),5000,5000);
            if(StringUtil.isBlank(reqResult)){
                logger.info("dealJSDData finPromiseIncomeService。reqResult is NULL");
                return ;
            }
            JSONObject paramsJson = JSON.parseObject(reqResult);
            String code = paramsJson.getString("code");
            if("200".equals(code)){
                String datas = paramsJson.getString("data");
                List<FinPromiseIncomeDo> list = JSONArray.parseArray(datas,FinPromiseIncomeDo.class);

                if(list.size()>0 && finPromiseIncomeService.batchinsert(list)>0){
                    logger.info("dealJSDData finPromiseIncomeService。batchInsert is success");
                } else {
                    logger.info("dealJSDData finPromiseIncomeService。batchInsert is failed or list.size<0");
                }
            } else {
                logger.info("response code is not 200");
            }
        }
        public static String toJSONString(Object jsonObj){
            return JSON.toJSONString(jsonObj);
        }
    }

