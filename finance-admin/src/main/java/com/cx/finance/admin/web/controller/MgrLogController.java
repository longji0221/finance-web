package com.cx.finance.admin.web.controller;

import com.cx.finance.admin.web.dto.req.MgrLogReq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.MgrLogService;
import com.cx.finance.dal.domain.MgrLogDo;
import com.cx.finance.dal.query.MgrLogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class MgrLogController {

    @Autowired
    MgrLogService logService;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Resp<?> list( @RequestBody MgrLogReq req) {
        MgrLogQuery query = req.toDalQuery();
        List<MgrLogDo> res = logService.listByQueruy(query);
        return Resp.succ(QueryRespData.gen(res, query.getTotalCount()), null);
    }
}
