package com.cx.finance.admin.web.controller;

import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.bo.FinRefundBo;
import com.cx.finance.biz.service.FinRefundService;
import com.cx.finance.biz.service.FinResourceService;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.domain.FinRefundDto;
import com.cx.finance.dal.query.FinRefundQuery;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * @类描述：财务管理 退款数据
 *
 * @author yinxiangyu
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Controller
@RequestMapping("/api/refund")
@ResponseBody
public class RefundDataController {

    @Resource
    FinRefundService finRefundService;


    @Resource
    private FinResourceService finResourceService;
    //数据列表
    @RequestMapping(value = "/listRefundData.json")
    public Resp<?> listOfflineRepayData(@RequestBody FinRefundQuery query) {
        List<FinRefundDto> list=null;
        if(StringUtil.isNotBlank(query.getMobile()) || query.getStatus()!=null){
            query.setPageSize(query.limit);
            query.setPageIndex(query.page);
            list=finRefundService.getRefundByUserId(query);
        }
        return Resp.succ(QueryRespData.gen(list, query.getTotalCount()), "");
    }


    //数据列表
    @RequestMapping(value = "/listRefund.json")
    public Resp<?> listRefundRevew(@RequestBody FinRefundQuery query) {
        List<FinRefundDto> list=null;
        query.setPageSize(query.limit);
        query.setPageIndex(query.page);
        list=finRefundService.getRefundByUserId(query);
        return Resp.succ(QueryRespData.gen(list, query.getTotalCount()), "");
    }
    @RequestMapping(value = "/reviewRefuse.json")
    public Resp<?> reviewRefuse(HttpServletRequest request) {
        String amount = request.getParameter("amount");
        String id = request.getParameter("id");
        String mobile = request.getParameter("mobile").trim();
        FinRefundBo bo=new FinRefundBo();
        bo.reFundAmount=amount;
        bo.id=Long.parseLong(id);
        bo.mobile=mobile;
        bo.finance=Sessions.getRealname(request);
        finRefundService.refuseRefund(bo);
        return Resp.succ();
    }

    @RequestMapping(value = "/reviewPass.json")
    public Resp<?> passRefuse(HttpServletRequest request) {
      try {
          String amount = request.getParameter("amount");
          String id = request.getParameter("id");
          String mobile = request.getParameter("mobile").trim();
          String remark = request.getParameter("remark");
          String type = request.getParameter("type");
          FinRefundBo bo=new FinRefundBo();
          bo.reFundAmount=amount;
          bo.id=Long.parseLong(id);
          bo.mobile=mobile;
          bo.finance=Sessions.getRealname(request);
          bo.remark=remark;
          bo.type=type;
          bo.dataUrl=finResourceService.getApiUrl();
          finRefundService.passRefund(bo);
          return Resp.succ();
      }catch (BizException e){
          return  Resp.fail("第三方请求失败");
      }catch (Exception e){
          return  Resp.fail("系统错误");
      }
    }
}
