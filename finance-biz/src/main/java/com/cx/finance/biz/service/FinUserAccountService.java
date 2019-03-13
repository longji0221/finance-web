package com.cx.finance.biz.service;

import com.cx.finance.dal.domain.FinUserAccountDo;

/**
 * Service
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:07:18
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface FinUserAccountService extends ParentService<FinUserAccountDo, Long>{

    void updateOutAccountByUserId(FinUserAccountDo userAccountDo);

    void updateInAccountByUserId(FinUserAccountDo userAccountDo);


    FinUserAccountDo getByUserId(Long userId);


    FinUserAccountDo getByMobile(String mobile);

}
