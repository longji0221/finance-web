package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinActualIncomeDo;
import com.cx.finance.dal.query.ActualIncomeQuery;

import java.util.List;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-01 16:36:24
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinActualIncomeDao extends BaseDao<FinActualIncomeDo, Long> {

    /**
     * 查询实收数据
     * @Param query {@link ActualIncomeQuery} 对象
     *@return 根据查询条件查询，没查询条件查全量 <code>List<code/>
     *
     * **/
    List<FinActualIncomeDo> getListByQuery(ActualIncomeQuery query);

    /**
     * 批量插入实收数据
     * @Param list {@link FinActualIncomeDo} 对象
     *@return  <code>List<code/>
     *
     * **/
    int batchinsert(List<FinActualIncomeDo> list);
}
