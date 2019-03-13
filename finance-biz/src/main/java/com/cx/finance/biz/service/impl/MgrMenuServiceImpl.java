package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.domain.MgrRoleMenuDo;
import org.springframework.stereotype.Service;

import com.cx.finance.biz.service.MgrMenuService;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.MgrMenuDao;
import com.cx.finance.dal.domain.MgrMenuDo;

import java.math.BigDecimal;
import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("mgrMenuService")
public class MgrMenuServiceImpl extends ParentServiceImpl<MgrMenuDo, Long> implements MgrMenuService {
	
    @Resource
    private MgrMenuDao mgrMenuDao;

		@Override
	public BaseDao<MgrMenuDo, Long> getDao() {
		return mgrMenuDao;
	}

	@Override
	public List<MgrMenuDo> getMenuByParentId(Long id) {
		return mgrMenuDao.getMenuByParentId(id);
	}

	@Override
	public List<MgrRoleMenuDo> getCheckedMenu(Long roleId) {
		return mgrMenuDao.getCheckedMenu(roleId);
	}
}