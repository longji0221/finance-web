package com.cx.finance.admin.web.controller.third;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.admin.spring.NotNeedLogin;
import com.cx.finance.admin.web.controller.BaseController;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinPangedTradePoolService;
import com.cx.finance.biz.service.FinResourceService;
import com.cx.finance.common.util.FinanceThirdUtil;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNeedLogin
@RestController
@RequestMapping("/third/collect")
public class CollectionOfflinePoolController extends BaseController {

    @Resource
    private FinPangedTradePoolService finPangedTradePoolService;

    @Resource
    private FinResourceService finResourceService;

    private final static String SUCCESS_CODE="200";

    @RequestMapping(value = "/tradeNo")
    @ResponseBody
   public  Resp<?>  collect(HttpServletRequest request){
        String tradeNo = request.getParameter("tradeNo");
        logger.info("third request  '/third/collect/tradeNo'  tradeNo="+tradeNo);
        FinPangedTradePoolDo finPangedTradePoolDo=finPangedTradePoolService.getPangedTradePoolDataByTradeNo(tradeNo);
        if(finPangedTradePoolDo==null){
            return Resp.succ(200,"succ","");
        }
        Map data=new HashMap();
        List repayInfos=new ArrayList();
        data.put("repayMoney",finPangedTradePoolDo.getAmount());
        data.put("isUse","true");
        data.put("tradeNo",tradeNo);
        Map reqData=new HashMap();
        reqData.put("tradeNo",tradeNo);
        Map<String, String> dataUrl=finResourceService.getApiUrl();
        for(String key:dataUrl.keySet()){
            String resultStrJsd =FinanceThirdUtil.offlineRepayInfoFoApi(reqData,dataUrl.get(key));
            if(StringUtil.isNotBlank(resultStrJsd)) {
                Map respBoJsd= (Map) JSONUtils.parse(resultStrJsd);
                if(respBoJsd.get("code").equals(SUCCESS_CODE)){
                    Object repayObject=respBoJsd.get("data");
                    Map<String,String> repayInfo= (Map<String, String>) JSONUtils.parse(repayObject.toString());
                    if("JSD".equals(key)){
                        repayInfo.put("company","jsd");
                    }else if("JGD".equals(key)) {
                        repayInfo.put("company","Jgd");
                    }else if("DFD".equals(key)) {
                        repayInfo.put("company","Dfd");
                    }
                    repayInfos.add(repayInfo);
                }
            }
        }
        data.put("repayInfos",repayInfos);
        return Resp.fail(data,900,"");


   }

}
