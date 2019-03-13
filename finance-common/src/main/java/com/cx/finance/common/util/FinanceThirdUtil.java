package com.cx.finance.common.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class FinanceThirdUtil {

    protected static final Logger logger = LoggerFactory.getLogger(FinanceThirdUtil.class);

    public static String  syncUserForApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/getUserInfo",params);
        logger.info("syncUserForApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String  requestUserBorrowInfosForApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/getUserBorrowInfos", params);
        logger.info("requestUserBorrowInfosForApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String  requestUserLikeBorrowInfosForApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/getUserLikeBorrowInfos", params);
        logger.info("requestUserBorrowInfosForApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }
    public static String  requestBorrowDetailForApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/getBorrowDetail",params);
        logger.info("requestBorrowDetailForApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String  requestInAccountForApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/inAccount",params);
        logger.info("requestInAccountForApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String  offlineRepayFoApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/offlineRepay",params);
        logger.info("offlineRepayFoApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String  offlineRepayInfoFoApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/getOfflineRepayInfo",params);
        logger.info("offlineRepayFoApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String  offlineRefundForApi(Map<String,String> data,String host){
        String reqStr = JSON.toJSONString(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", reqStr);
        params.put("sign", sign(reqStr));
        String resultStr=HttpUtil.post(host+"/third/finance/offlineRefund",params);
        logger.info("offlineRefundForApi, PARAMS=" + params + ", RESP=" + resultStr);
        return resultStr;
    }

    public static String sign(String data) {
       return Base64.encode(AesUtil.encrypt(data, getKey()));
    }

    public static String getKey() {
        return "testC1b6x@6aH$2dlw";
    }
}
