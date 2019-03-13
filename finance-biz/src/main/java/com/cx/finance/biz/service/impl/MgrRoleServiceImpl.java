package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.RoleQuery;
import org.springframework.stereotype.Service;

import com.cx.finance.biz.service.MgrRoleService;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.MgrRoleDao;
import com.cx.finance.dal.domain.MgrRoleDo;

import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("mgrRoleService")
public class MgrRoleServiceImpl extends ParentServiceImpl<MgrRoleDo, Long> implements MgrRoleService {
	
    @Resource
    private MgrRoleDao mgrRoleDao;

		@Override
	public BaseDao<MgrRoleDo, Long> getDao() {
		return mgrRoleDao;
	}

	@Override
	public List<MgrRoleDo> getRoleList(RoleQuery query) {
		return mgrRoleDao.getRoleList(query);
	}

	@Override
	public List<MgrRoleDo> getRole() {
		return mgrRoleDao.getRole();
	}
}