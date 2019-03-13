package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.MgrLogDo;
import com.cx.finance.dal.query.MgrLogQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-12-17 09:52:41
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Repository
public interface MgrLogDao extends BaseDao<MgrLogDo, Long> {

    void save(MgrLogDo logDO);
    List<MgrLogDo> queryList(MgrLogQuery query);
    int remove(Long id);
    int batchRemove(Long[] ids);

    List<MgrLogDo> listByQueruy(MgrLogQuery query);
}
