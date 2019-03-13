package com.cx.finance.admin.web.controller;

import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinPangedTradePoolService;
import com.cx.finance.biz.service.FinRepaymentOfflineTradePoolService;
import com.cx.finance.common.enums.FinOfflineRepayType;
import com.cx.finance.common.enums.YesNoStatus;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.ExportUtil;
import com.cx.finance.dal.domain.FinActualPaidDo;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import com.cx.finance.dal.query.ActualPaidQuery;
import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @类描述：客服工具 线下还款数据查询
 *
 * @author yinxiangyu
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@RequestMapping("/api/offlineRepaymentDataQuery")
@ResponseBody
public class OfflineRepaymentDataQueryController {

    @Resource
    FinRepaymentOfflineTradePoolService finRepaymentOfflineTradePoolService;
    @Resource
    FinPangedTradePoolService finPangedTradePoolService;

    @RequestMapping(value = "/getOfflineRepaymentData.json")
    public Resp<?> getofflineRepaymentDataList(@RequestBody FinRepaymentOfflineTradePoolQuery query){
        List<FinRepaymentOfflineTradePoolQuery> list=null;
        if (FinOfflineRepayType.ALIPAY.code.equals(query.getRepayType())){
            query.setPageSize(query.limit);
            query.setPageIndex(query.page);
            list=finRepaymentOfflineTradePoolService.offlineRepaymentDataQuery(query);
            for (FinRepaymentOfflineTradePoolQuery finRepaymentOfflineTradePoolQuery : list) {
                 FinPangedTradePoolDo pangedTradePoolDataByTradeNo = finPangedTradePoolService.getPangedTradePoolDataByTradeNo(finRepaymentOfflineTradePoolQuery.getTradeNo());
                 if (pangedTradePoolDataByTradeNo != null){
                     finRepaymentOfflineTradePoolQuery.setIsUse("已使用");
                 }else {
                     finRepaymentOfflineTradePoolQuery.setIsUse("没使用");
                 }
            }
        }
        return Resp.succ(QueryRespData.gen(list,query.getTotalCount()),"");
    }


}
