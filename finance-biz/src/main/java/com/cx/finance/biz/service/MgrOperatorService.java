package com.cx.finance.biz.service;

import com.cx.finance.dal.domain.MgrOperatorDo;

/**
 * Service
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MgrOperatorService extends ParentService<MgrOperatorDo, Long>{
	MgrOperatorDo getByUsername(String username);

	String getDivMenusIdByOperatorId(Long operatorId,String menuUrl);

	void clearMenus(Long operatorId);


}
