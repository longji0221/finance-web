package com.cx.finance.biz.service;

import com.cx.finance.dal.domain.FinActualPaidDo;
import com.cx.finance.dal.query.ActualPaidQuery;

import java.util.List;

/**
 * Service
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-10-29 14:29:27
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinActualPaidService extends ParentService<FinActualPaidDo, Long>{

    /**
     * 查询实付数据
     * @Param query {@link ActualPaidQuery} 对象
     *@return 根据查询条件查询，没查询条件查全量 <code>List<code/>
     *
     * **/
    List<FinActualPaidDo> getListByQuery(ActualPaidQuery query);

    /**
     * 批量插入实付数据
     * @Param list {@link FinActualPaidDo} 对象
     *@return  <code>List<code/>
     *
     * **/
    int batchinsert(List<FinActualPaidDo> list);

}
