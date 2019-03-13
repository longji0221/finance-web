package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinPromiseIncomeDo;
import com.cx.finance.dal.query.PromiseIncomeQuery;

import java.util.List;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-01 18:30:11
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinPromiseIncomeDao extends BaseDao<FinPromiseIncomeDo, Long> {

    /**
     * 查询应收数据
     * @Param query {@link PromiseIncomeQuery} 对象
     *@return 根据查询条件查询，没查询条件查全量 <code>List<code/>
     *
     * **/
    List<FinPromiseIncomeDo> getListByQuery(PromiseIncomeQuery query);

    /**
     * 批量插入应收数据
     * @Param list {@link FinPromiseIncomeDo} 对象
     *@return  <code>List<code/>
     *
     * **/
    int batchinsert(List<FinPromiseIncomeDo> list);

}
