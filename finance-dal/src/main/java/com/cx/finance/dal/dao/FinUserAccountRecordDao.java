package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinUserAccountRecordDo;
import com.cx.finance.dal.query.UserQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:06:53
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinUserAccountRecordDao extends BaseDao<FinUserAccountRecordDo, Long> {


    List<FinUserAccountRecordDo> getUserAccountRecodListByMobile(UserQuery dalQuery);
}
