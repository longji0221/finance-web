package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cx.finance.biz.service.MgrOperatorRoleService;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.MgrOperatorRoleDao;
import com.cx.finance.dal.domain.MgrOperatorRoleDo;



/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("mgrOperatorRoleService")
public class MgrOperatorRoleServiceImpl extends ParentServiceImpl<MgrOperatorRoleDo, Long> implements MgrOperatorRoleService {
	
    @Resource
    private MgrOperatorRoleDao mgrOperatorRoleDao;

		@Override
	public BaseDao<MgrOperatorRoleDo, Long> getDao() {
		return mgrOperatorRoleDao;
	}
}