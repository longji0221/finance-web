package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.FinUserAccountDo;
import org.apache.ibatis.annotations.Param;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:07:18
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinUserAccountDao extends BaseDao<FinUserAccountDo, Long> {


    void updateOutAccountByUserId(FinUserAccountDo userAccountDo);

    FinUserAccountDo getByUserId(@Param("userId") Long userId);

    FinUserAccountDo getByMobile(@Param("mobile")String mobile);

    void updateInAccountByUserId(FinUserAccountDo userAccountDo);

}
