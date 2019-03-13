package com.cx.finance.admin.web.controller;


import com.cx.finance.admin.web.dto.req.PromiseIncomeQeq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinPromiseIncomeService;
import com.cx.finance.common.annotation.Log;
import com.cx.finance.common.enums.ProduceType;
import com.cx.finance.common.enums.liquidationCompany;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.ExportUtil;
import com.cx.finance.dal.domain.FinPromiseIncomeDo;
import com.cx.finance.dal.query.PromiseIncomeQuery;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/promiseIncome")
public class PromiseIncomeController extends BaseController{

    @Autowired
    FinPromiseIncomeService finPromiseIncomeService;

    @Log("查询应收数据")
    @RequestMapping(value = "/list.json",method = RequestMethod.POST)
    public Resp<?> list(@RequestBody PromiseIncomeQeq req) {
        PromiseIncomeQuery query = req.toDalQuery();
        List<FinPromiseIncomeDo> res = finPromiseIncomeService.getListByQuery(query);
        return Resp.succ(QueryRespData.gen(res, query.getTotalCount()), null);
    }

    @Log("导出应收数据")
    @RequestMapping(value = "/export.json", method = RequestMethod.GET)
    public void exportDetailList( HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {

        PromiseIncomeQuery query = buildQuery(request);
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
            String sTitle = "清算公司,产品类型,产品名称,借款编号,期数,应还时间,应收总额,累计已还金额,剩余应还金额,应收借款本金,应收利息,应收手续费,应收搭售商品费用,应收逾期费,状态";
            String fName = "实付信息_";
            String mapKey = "liquidationCompany,productType,productName,borrowNo,periods,gmtPlanRepay,predictAmount,repayAmount,noneAmount,amount,retaAmount,poundageAmount,shopAmount,overdueAmount,status";
            Map<String, Object> map;

            List<FinPromiseIncomeDo> list = finPromiseIncomeService.getListByQuery(query);

            for (FinPromiseIncomeDo finPromiseIncomeDo : list) {
                map = new HashMap<String, Object>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                map.put("liquidationCompany",finPromiseIncomeDo.getLiquidationCompany());
                map.put("productType", finPromiseIncomeDo.getProductType());
                map.put("productName", finPromiseIncomeDo.getProductName());
                map.put("borrowNo", finPromiseIncomeDo.getBorrowNo());
                map.put("periods", finPromiseIncomeDo.getPeriods());
                map.put("gmtPlanRepay", sdf.format(finPromiseIncomeDo.getGmtPlanRepay()));
                map.put("predictAmount", finPromiseIncomeDo.getPredictAmount());
                map.put("repayAmount", finPromiseIncomeDo.getRepayAmount());
                map.put("noneAmount", finPromiseIncomeDo.getNoneAmount());
                map.put("amount", finPromiseIncomeDo.getAmount());
                map.put("retaAmount", finPromiseIncomeDo.getRetaAmount());
                map.put("poundageAmount", finPromiseIncomeDo.getPoundageAmount());
                map.put("shopAmount", finPromiseIncomeDo.getShopAmount());
                map.put("overdueAmount", finPromiseIncomeDo.getOverdueAmount());
                map.put("status", finPromiseIncomeDo.getStatus());

                dataList.add(map);
            }
            try (final OutputStream os = response.getOutputStream()) {
                ExportUtil.responseSetProperties("应收明细", response);
                ExportUtil.doExport(dataList, sTitle, mapKey, os);

            } catch (Exception e) {
                logger.error("导出CSV失败", e);
            }

        } catch (Exception e) {
            logger.error("导出CSV出错", e);
        }
    }


    private PromiseIncomeQuery buildQuery(HttpServletRequest request) {
        PromiseIncomeQuery query = new PromiseIncomeQuery();
        String liquidationCompany = ObjectUtils.toString(request.getParameter("liquidationCompany"), null);
        String borrowNo = ObjectUtils.toString(request.getParameter("borrowNo"), null);
        String gmtStart = ObjectUtils.toString(request.getParameter("gmtStart"), null);
        String gmtEnd = ObjectUtils.toString(request.getParameter("gmtEnd"), null);

        query.setLiquidationCompany(liquidationCompany);
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


