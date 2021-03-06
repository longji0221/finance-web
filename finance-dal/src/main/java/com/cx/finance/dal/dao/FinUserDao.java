package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinUserDo;
import org.apache.ibatis.annotations.Param;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-10-29 17:11:13
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinUserDao extends BaseDao<FinUserDo, Long> {

    FinUserDo  getUserByMobile(@Param("mobile") String mobile);

}
