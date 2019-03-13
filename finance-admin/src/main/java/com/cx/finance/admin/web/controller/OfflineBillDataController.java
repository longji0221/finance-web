package com.cx.finance.admin.web.controller;

import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinPangedTradePoolService;
import com.cx.finance.biz.service.FinRepaymentOfflineTradePoolService;
import com.cx.finance.common.enums.ProduceType;
import com.cx.finance.common.enums.liquidationCompany;
import com.cx.finance.common.util.DateUtil;
import com.cx.finance.common.util.ExportUtil;
import com.cx.finance.dal.domain.FinActualPaidDo;
import com.cx.finance.dal.query.ActualPaidQuery;
import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;
import com.cx.finance.dal.query.OfflineBillDataQuery;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @类描述：财务管理 线下还款数据导入
 *
 * @author yinxiangyu
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@RequestMapping("/api/offlineBIllData")
public class OfflineBillDataController extends BaseController{

    @Resource
    FinPangedTradePoolService finPangedTradePoolService;
    @Resource
    FinRepaymentOfflineTradePoolService finRepaymentOfflineTradePoolService;

    @RequestMapping(value = "/listOfflineBillData.json")
    @ResponseBody
    public Resp<?> listOfflineBillData(@RequestBody FinRepaymentOfflineTradePoolQuery query) {
        query.setPageSize(query.limit);
        query.setPageIndex(query.page);
        if(query.getDateStart()==null && query.getDateEnd()==null){
            query.setDateEnd(DateUtil.formatDateToYYYYMMdd(new Date()));
            query.setDateStart(DateUtil.addDays(DateUtil.formatDateToYYYYMMdd(new Date()),-15));
        }else {
            query.setDateEnd(DateUtil.addDays(query.getDateEnd(),1));
        }
        List list=getListByQuery(query);
        return Resp.succ(QueryRespData.gen(list, list.size()),"");
    }




    @RequestMapping(value = "/export.json", method = RequestMethod.GET)
    public void exportDetailList( HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {

        FinRepaymentOfflineTradePoolQuery query = buildQuery(request);
        if (query.getDateStart()== null || query.getDateEnd() == null) {
            return ;  //导出时间不能为空
        }

        String lockKey = "EXPORT_RECEIPT_DETAIL_LOCK";
        try {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(query.getDateStart());
            Calendar c2 = Calendar.getInstance();
            c2.setTime(query.getDateEnd());

            long days = DateUtil.getNumberOfDaysBetween(c1, c2);

            if (days > 1000) {
                logger.info("最多导出1000内天记录");
                return ; //如果导出时间大于32天就不能导出
            }

            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            String sTitle = "日期,当日入账（当日被认领）,支付宝入账（当日被认领）,银行卡";
            String fName = "线下入账数据_";
            String mapKey = "time,sumAmount,alipayAmount,bankAmount";
            Map<String, Object> map;

            List<HashMap> list=getListByQuery(query);

            for (HashMap ob : list) {
                map = new HashMap<String, Object>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("time",ob.get("time"));
                map.put("sumAmount",ob.get("sumAmount"));
                map.put("alipayAmount", ob.get("alipayAmount"));
                map.put("bankAmount", ob.get("bankAmount"));

                dataList.add(map);
            }
            try (final OutputStream os = response.getOutputStream()) {
                ExportUtil.responseSetProperties("线下入账明细", response);
                ExportUtil.doExport(dataList, sTitle, mapKey, os);

            } catch (Exception e) {
                logger.error("导出CSV失败", e);
            }

        } catch (Exception e) {
            logger.error("导出CSV出错", e);
        }
    }

    private FinRepaymentOfflineTradePoolQuery buildQuery(HttpServletRequest request) {
        FinRepaymentOfflineTradePoolQuery query = new FinRepaymentOfflineTradePoolQuery();
        String gmtStart = ObjectUtils.toString(request.getParameter("dateStart"), null);
        String gmtEnd = ObjectUtils.toString(request.getParameter("dateEnd"), null);

        SimpleDateFormat sdfs = new SimpleDateFormat(DateUtil.DEFAULT_PATTERN_WITH_HYPHEN);


        if (gmtStart != null) {
            try {
                query.setDateStart(sdfs.parse(gmtStart));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (gmtEnd != null) {
            try {
                query.setDateEnd(sdfs.parse(gmtEnd));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        query.setPageSize(5000);
        return query;
    }

    private List<HashMap> getListByQuery(FinRepaymentOfflineTradePoolQuery query){
        //总入账
        List<HashMap> inAccounts=finRepaymentOfflineTradePoolService.getDayAmount(query);
        //总领取
        List<HashMap> receives=finPangedTradePoolService.getDayReceive(query);
        // 支付宝总入账
        List<HashMap> inAccountsByAlipay=finRepaymentOfflineTradePoolService.getDayAmountByAlipay(query);
        // 支付宝被领取
        List<HashMap> receivesByAlipay=finPangedTradePoolService.getDayReceiveByAlipay(query);

        List<HashMap> list=new ArrayList<>();
        for (HashMap inAccount:inAccounts){
            HashMap data=new HashMap();
            String time= ObjectUtils.toString( inAccount.get("time"));
            BigDecimal receiveAmount=BigDecimal.ZERO;
            BigDecimal inAccountByAlipayAmount=BigDecimal.ZERO;
            BigDecimal receiveByAlipayAmount=BigDecimal.ZERO;
            for(HashMap inAccountByAlipay:inAccountsByAlipay){
                String inAccountByAlipayTime= ObjectUtils.toString(inAccountByAlipay.get("time"));
                if(!time.equals(inAccountByAlipayTime)){
                    continue;
                }
                inAccountByAlipayAmount= (BigDecimal) inAccountByAlipay.get("amount");
            }
            for(HashMap receive:receives){
                String receiveTime= ObjectUtils.toString(receive.get("time"));
                if(!time.equals(receiveTime)){
                    continue;
                }
                receiveAmount= (BigDecimal) receive.get("amount");
            }

            for(HashMap receiveByAlipay:receivesByAlipay){
                String receiveByAlipayTime= ObjectUtils.toString(receiveByAlipay.get("time"));
                if(!time.equals(receiveByAlipayTime)){
                    continue;
                }
                receiveByAlipayAmount= (BigDecimal) receiveByAlipay.get("amount");
            }
            BigDecimal inAccountAmount= (BigDecimal) inAccount.get("amount");
            data.put("time",time);
            data.put("sumAmount",inAccountAmount+"("+receiveAmount+")");
            data.put("alipayAmount",inAccountByAlipayAmount+"("+receiveByAlipayAmount+")");
            data.put("bankAmount",inAccountAmount.subtract(inAccountByAlipayAmount));
            list.add(data);
        }
        return  list;
    }
}
