package com.cx.finance.admin.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cx.finance.common.annotation.Log;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cx.finance.admin.web.dto.req.ActualPaidReq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinActualPaidService;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.ExportUtil;
import com.cx.finance.dal.domain.FinActualPaidDo;
import com.cx.finance.dal.query.ActualPaidQuery;

@RestController
@RequestMapping("/api/actualPaid")
public class ActualPaidController extends BaseController{

    @Autowired
    FinActualPaidService finActualPaidService;

    @Log("查询实付数据")
    @RequestMapping(value = "/list.json",method = RequestMethod.POST)
    public Resp<?> list( @RequestBody ActualPaidReq req) {
        ActualPaidQuery query = req.toDalQuery();
        List<FinActualPaidDo> res = finActualPaidService.getListByQuery(query);
        return Resp.succ(QueryRespData.gen(res, query.getTotalCount()), null);
    }

    @Log("导出实付数据")
    @RequestMapping(value = "/export.json", method = RequestMethod.GET)
    public void exportDetailList( HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {

        ActualPaidQuery query = buildQuery(request);
        if (query.getGmtStart() == null || query.getGmtEnd() == null) {
            return;   //导出时间不能为空
        }

        String lockKey = "EXPORT_RECEIPT_DETAIL_LOCK";
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(query.getGmtStart());
            Calendar c2 = Calendar.getInstance();
            c2.setTime(query.getGmtEnd());

            long days = DateUtil.getNumberOfDaysBetween(c1, c2);

            if (days > 32) {

                logger.info("导出时间大于32天");
                return;  //如果导出时间大于32天就不能导出
            }

            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String sTitle = "清算公司,产品类型,产品名称,借款编号,实际金额,实际付款时间,支付渠道,支付流水号";
            String fName = "实付信息_";
            String mapKey = "liquidationCompany,productType,productName,borrowNo,actualAmount,actualTime,tppNid,payTradeNo";
            Map<String, Object> map;

            List<FinActualPaidDo> list = finActualPaidService.getListByQuery(query);

            for (FinActualPaidDo finActualPaidDo : list) {
                map = new HashMap<String, Object>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("liquidationCompany",finActualPaidDo.getLiquidationCompany());
                map.put("productType",finActualPaidDo.getProductType());
                map.put("productName", finActualPaidDo.getProductName());
                map.put("borrowNo", finActualPaidDo.getBorrowNo());
                map.put("actualAmount", finActualPaidDo.getActualAmount());
                map.put("actualTime", sdf.format(finActualPaidDo.getActualTime()));
                map.put("tppNid", finActualPaidDo.getTppNid());
                map.put("payTradeNo", finActualPaidDo.getPayTradeNo());

                dataList.add(map);
            }
            try (final OutputStream os = response.getOutputStream()) {
                ExportUtil.responseSetProperties("实付明细", response);
                ExportUtil.doExport(dataList, sTitle, mapKey, os);

            } catch (Exception e) {
                logger.error("导出CSV失败", e);
            }

        } catch (Exception e) {
            logger.error("导出CSV出错", e);
        }
    }


    private ActualPaidQuery buildQuery(HttpServletRequest request) {
        ActualPaidQuery query = new ActualPaidQuery();
        String liquidationCompany = ObjectUtils.toString(request.getParameter("liquidationCompany"), null);
        String productType = ObjectUtils.toString(request.getParameter("productType"), null);
        String borrowNo = ObjectUtils.toString(request.getParameter("borrowNo"), null);
        String gmtStart = ObjectUtils.toString(request.getParameter("gmtStart"), null);
        String gmtEnd = ObjectUtils.toString(request.getParameter("gmtEnd"), null);

        query.setLiquidationCompany(liquidationCompany);
        query.setProductType(productType);
        SimpleDateFormat sdfs = new SimpleDateFormat(DateUtil.DEFAULT_PATTERN_WITH_HYPHEN);


        if (gmtStart != null) {
            try {
                query.setGmtStart(sdfs.parse(gmtStart));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (gmtEnd != null) {
            try {
                query.setGmtEnd(sdfs.parse(gmtEnd));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        query.setBorrowNo(borrowNo);
        query.setPageSize(500000);
        return query;
    }
}
