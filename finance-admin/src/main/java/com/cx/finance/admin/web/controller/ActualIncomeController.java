package com.cx.finance.admin.web.controller;

import com.cx.finance.admin.web.dto.req.ActualIncomeReq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinActualIncomeService;
import com.cx.finance.common.annotation.Log;
import com.cx.finance.common.enums.ProduceType;
import com.cx.finance.common.enums.liquidationCompany;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.ExportUtil;
import com.cx.finance.dal.domain.FinActualIncomeDo;
import com.cx.finance.dal.query.ActualIncomeQuery;
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
@RequestMapping("/api/actualIncome")
public class ActualIncomeController extends BaseController{

    @Autowired
    FinActualIncomeService finActualIncomeService;

    @Log("查询实收数据")
    @RequestMapping(value = "/list.json",method = RequestMethod.POST)
    public Resp<?> list(@RequestBody ActualIncomeReq req) {
        ActualIncomeQuery query = req.toDalQuery();
        List<FinActualIncomeDo> res = finActualIncomeService.getListByQuery(query);
        return Resp.succ(QueryRespData.gen(res, query.getTotalCount()), null);
    }


    @Log("导出实收数据")
    @RequestMapping(value = "/export.json", method = RequestMethod.GET)
    public void exportDetailList( HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {

        ActualIncomeQuery query = buildQuery(request);
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
            String sTitle = "清算公司,产品类型,产品名称,收款流水号,支付方式,实收金额,实际收款时间,支付渠道,支付流水号";
            String fName = "实付信息_";
            String mapKey = "liquidationCompany,productType,productName,repayNo,payType,actualAmount,actualTime,tppNid,payTradeNo";
            Map<String, Object> map;

            List<FinActualIncomeDo> list = finActualIncomeService.getListByQuery(query);

            for (FinActualIncomeDo finActualIncomeDo : list) {
                map = new HashMap<String, Object>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               /* if(liquidationCompany.ISHANGJIE.getCode().equals(finActualIncomeDo.getLiquidationCompany())){
                    map.put("liquidationCompany",liquidationCompany.ISHANGJIE.getName());
                } else  if(liquidationCompany.LVYOU.getCode().equals(finActualIncomeDo.getLiquidationCompany())){
                    map.put("liquidationCompany",liquidationCompany.LVYOU.getName());
                }else  if(liquidationCompany.YIGANG.getCode().equals(finActualIncomeDo.getLiquidationCompany())){
                    map.put("liquidationCompany",liquidationCompany.YIGANG.getName());
                } else {
                    map.put("liquidationCompany","其他");
                }

                if(ProduceType.BORROWCASH.getCode().equals( finActualIncomeDo.getProductType())){
                    map.put("productType", ProduceType.BORROWCASH.getName());
                } else if(ProduceType.BORROW.getCode().equals( finActualIncomeDo.getProductType())){
                    map.put("productType", ProduceType.BORROW.getName());
                }else if(ProduceType.CASHINSTALMENT.getCode().equals( finActualIncomeDo.getProductType())){
                    map.put("productType", ProduceType.CASHINSTALMENT.getName());
                }else {
                    map.put("productType","其他");
                }*/
                map.put("liquidationCompany",finActualIncomeDo.getLiquidationCompany());
                map.put("productType",finActualIncomeDo.getProductType());
                map.put("productName", finActualIncomeDo.getProductName());
                map.put("repayNo", finActualIncomeDo.getRepayNo());
                map.put("payType", finActualIncomeDo.getPayType());
                map.put("actualAmount", finActualIncomeDo.getActualAmount());
                map.put("actualTime", sdf.format(finActualIncomeDo.getActualTime()));
                map.put("tppNid", finActualIncomeDo.getTppNid());
                map.put("payTradeNo", finActualIncomeDo.getPayTradeNo());

                dataList.add(map);
            }
            try (final OutputStream os = response.getOutputStream()) {
                ExportUtil.responseSetProperties("实收明细", response);
                ExportUtil.doExport(dataList, sTitle, mapKey, os);

            } catch (Exception e) {
                logger.error("导出CSV失败", e);
            }

        } catch (Exception e) {
            logger.error("导出CSV出错", e);
        }
    }


    private ActualIncomeQuery buildQuery(HttpServletRequest request) {
        ActualIncomeQuery query = new ActualIncomeQuery();
        String liquidationCompany = ObjectUtils.toString(request.getParameter("liquidationCompany"), null);
        String productType = ObjectUtils.toString(request.getParameter("productType"), null);
        String repayNo = ObjectUtils.toString(request.getParameter("repayNo"), null);
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
        query.setRepayNo(repayNo);
        query.setPageSize(500000);
        return query;
    }
}

