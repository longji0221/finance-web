package com.cx.finance.dal.dao;

import com.cx.finance.dal.domain.MgrOperatorRoleDo;

/**
 * Dao
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MgrOperatorRoleDao extends BaseDao<MgrOperatorRoleDo, Long> {
	public MgrOperatorRoleDo getByOperatorId(Long operatorId);
}
