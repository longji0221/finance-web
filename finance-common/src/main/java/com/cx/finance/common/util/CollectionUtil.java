package com.cx.finance.common.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.common.ConfigProperties;
import com.cx.finance.common.Constants;
import com.cx.finance.common.exception.BizException;
import java.util.HashMap;

import java.util.Map;

public class CollectionUtil {
    public static final String COLLECTION_REQ_CODE_SUCC = "200";

    private static String url = null;
    private static String getCollectionReportUrl() {
        if (url == null) {
            url = ConfigProperties.get(Constants.CONFKEY_COLLECTION_REPORT_URL);
            return url;
        }
        return url;
    }

    /**
     * 极速贷逾期通知催收平台
     * @return
     */
    public static boolean requestCollectionOfflineTradeNo(Map<String,String> data) {
        try {
            Map<String,String>  params=new HashMap<>();
            params.put("repaymentNo",data.get("tradeNo"));
            String token =  ConfigProperties.get(Constants.CONFKEY_COLLECTION_TOKEN);
            params.put("token",token);
            String url = getCollectionReportUrl() + "/api/ald/collect/v1/third/repaymentNo";
            String reqResult = HttpUtil.post(url, params);
            if (StringUtil.isBlank(reqResult)) {
                throw new BizException("requestCollectionOfflineTradeNo request fail , reqResult is null");
            }
            Map reault= (Map) JSONUtils.parse(reqResult);
            if("200".equals(reault.get("code")) || 200==Integer.parseInt(String.valueOf(reault.get("code")))){
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new BizException("requestCollectionOfflineTradeNo request fail Exception is " + e );
        }
    }
}
