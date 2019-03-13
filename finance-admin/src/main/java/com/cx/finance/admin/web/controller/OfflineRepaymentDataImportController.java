
package com.cx.finance.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.FinRepaymentOfflineTradePoolService;
import com.cx.finance.biz.service.FinResourceService;
import com.cx.finance.common.Constants;
import com.cx.finance.common.enums.FinOfflineRepayType;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.exception.BizExceptionCode;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.domain.FinRepaymentOfflineTradePoolDo;
import com.cx.finance.dal.domain.FinResourceDo;
import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @类描述：财务管理 线下还款数据导入
 *
 * @author yinxiangyu
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Controller
@RequestMapping("/api/offlineRepaymentDataImport")
@ResponseBody
public class OfflineRepaymentDataImportController extends BaseController {

    @Resource
    FinRepaymentOfflineTradePoolService finRepaymentOfflineTradePoolService;
    @Resource
    FinResourceService finResourceService;

    // 数据列表
    @RequestMapping(value = "/listOfflineRepayData.json")
    public Resp<?> listOfflineRepayData(@RequestBody FinRepaymentOfflineTradePoolQuery query) {
        if(query.getDateStart()!=null && query.getDateEnd()!=null){
            if(query.getDateEnd().before(query.getDateStart())){
                return Resp.fail("结束时间不能小于开始时间");
            }
        }
        query.setPageSize(query.limit);
        query.setPageIndex(query.page);
        List<FinRepaymentOfflineTradePoolQuery> list = finRepaymentOfflineTradePoolService.getListRepaymentOfflinePool(query);
        return Resp.succ(QueryRespData.gen(list, query.getTotalCount()), "");
    }

    /*
     *  获取收款账户
     */
    @RequestMapping(value = "/getBenefitAccount.json")
    public Resp<?> getBebefitAccount(HttpServletRequest request){
        return Resp.succ(finResourceService.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,Constants.BENEFIT_ACCOUNT),"");
    }

    /**
     *  上传并处理Excel账单
     */
    @RequestMapping(value = "/uploadExcel")
    public Resp<?> uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            String curProjectPath = "/home/aladin/project/tomcat/uploadExcel/";
            String fileName = curProjectPath + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(fileName));
            final List<FinRepaymentOfflineTradePoolDo> excelList = parseExcel(fileName);
            logger.info("Method = " + request.getRequestURL() + ", extracted data = " + JSON.toJSONString(excelList));
            new Thread(){ public void run() {
                insertDatabase(excelList,Sessions.getRealname(request));
            }}.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Resp.succ();
    }
    //添加到数据库
    void insertDatabase(List<FinRepaymentOfflineTradePoolDo> excelList,String name){
        for (FinRepaymentOfflineTradePoolDo finRepaymentOfflineTradePoolDo : excelList) {
            try {
                logger.info("Offline repay data import operator"+name);
                if(finRepaymentOfflineTradePoolService.getRepaymentOfflinePoolDataByTradeNo(finRepaymentOfflineTradePoolDo.getTradeNo())==null){
                    finRepaymentOfflineTradePoolDo.setOperator(name);
                    finRepaymentOfflineTradePoolService.saveRecord(finRepaymentOfflineTradePoolDo);
                }else {
                    logger.error("Data insertion exception，tradeNo:"+finRepaymentOfflineTradePoolDo.getTradeNo()+"trade_no重复：请查询");
                }
            }catch (Exception e){
                logger.error("Data insertion exception，case:"+e);
            }
        }
    }
    private static final String EXPORT_TIME_DES = "导出时间";// #导出时间

    private List<FinRepaymentOfflineTradePoolDo> parseExcel(String fileName) throws IOException {
        InputStream input = null;
        try {
            List<FinRepaymentOfflineTradePoolDo> excelList = new ArrayList<FinRepaymentOfflineTradePoolDo>();
            input = new FileInputStream(fileName);

            Workbook workbook = WorkbookFactory.create(input);
            Sheet st = workbook.getSheetAt(0);
            String benefitAccount=st.getRow(0).getCell(0).getStringCellValue();
            benefitAccount= getBenefitAccount(benefitAccount);
            List<String> cellsPerRowHolder = new ArrayList<String>();
            for (int j = 3; j < st.getLastRowNum() + 1; j++) {
                Row row = st.getRow(j);
                if (row != null) {
                    int rowSize = row.getLastCellNum();
                    Cell cell;
                    for (int i = 0; i < rowSize; i++) {
                        cell = row.getCell(i);
                        if (cell != null) {
                            String value = StringUtils.EMPTY;
                            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
                                    || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                                value = new DecimalFormat("#").format(cell.getNumericCellValue());
                            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                value = cell.getStringCellValue();
                            }
                            cellsPerRowHolder.add(value);
                        }
                    }

                    if (cellsPerRowHolder.get(0).contains(EXPORT_TIME_DES)) {
                        break;
                    }
                    FinRepaymentOfflineTradePoolDo afRepaymentOfflineTradePoolDo=new FinRepaymentOfflineTradePoolDo();
                    String mobileFromRemarkAccount = StringUtil.extractMobile(cellsPerRowHolder.get(16));
                    afRepaymentOfflineTradePoolDo.setPayerAccount(StringUtil.isBlank(mobileFromRemarkAccount)?cellsPerRowHolder.get(12):mobileFromRemarkAccount);
                    if (StringUtils.isBlank(afRepaymentOfflineTradePoolDo.getPayerAccount())){
                        continue;
                    }
                    afRepaymentOfflineTradePoolDo.setBenefitAccount(benefitAccount);
                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if(StringUtils.isBlank(cellsPerRowHolder.get(10))){
                        afRepaymentOfflineTradePoolDo.setRepayType(FinOfflineRepayType.ALIPAY.code);
                    }else {
                        afRepaymentOfflineTradePoolDo.setRepayType(cellsPerRowHolder.get(10));
                    }
                    afRepaymentOfflineTradePoolDo.setTradeNo(cellsPerRowHolder.get(2));
                    afRepaymentOfflineTradePoolDo.setAmount(StringUtils.isBlank(cellsPerRowHolder.get(6))? new BigDecimal(0):new BigDecimal(cellsPerRowHolder.get(6)));
                    afRepaymentOfflineTradePoolDo.setRemark(cellsPerRowHolder.get(16));
                    afRepaymentOfflineTradePoolDo.setRealName(cellsPerRowHolder.get(13));
                    afRepaymentOfflineTradePoolDo.setPayTime(format1.parse(cellsPerRowHolder.get(1)));
                    cellsPerRowHolder.clear();
                    excelList.add(afRepaymentOfflineTradePoolDo);
                }
            }
            return excelList;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BizException(BizExceptionCode.ALIPAY_BILL_PARSING_ERROE,e);
        }finally {
            if(input != null) input.close();
        }
    }


    private String getBenefitAccount(String benefitAccount){
        List<FinResourceDo> finResourceDo=finResourceService.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,Constants.BENEFIT_ACCOUNT);
        for (FinResourceDo resourceDo : finResourceDo) {
            if(benefitAccount.contains(resourceDo.getValue())){
                return resourceDo.getName();
            }
        }
        return "未知账户";
    }

}
