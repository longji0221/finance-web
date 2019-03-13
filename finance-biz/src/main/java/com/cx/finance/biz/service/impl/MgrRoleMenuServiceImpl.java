package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cx.finance.biz.service.MgrRoleMenuService;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.MgrRoleMenuDao;
import com.cx.finance.dal.domain.MgrRoleMenuDo;

import java.util.Date;
import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("mgrRoleMenuService")
public class MgrRoleMenuServiceImpl extends ParentServiceImpl<MgrRoleMenuDo, Long> implements MgrRoleMenuService {
	
    @Resource
    private MgrRoleMenuDao mgrRoleMenuDao;

		@Override
	public BaseDao<MgrRoleMenuDo, Long> getDao() {
		return mgrRoleMenuDao;
	}

	@Override
	public void setAuthMenus(String operator,Long roleId, String menuIds) {
		String[] menuIdList=menuIds.split(",");
		for(String menuId:menuIdList){
			MgrRoleMenuDo roleMenuDo=new MgrRoleMenuDo();
			roleMenuDo.setCreator(operator);
			roleMenuDo.setGmtCreate(new Date());
			roleMenuDo.setGmtModified(new Date());
			roleMenuDo.setModifier(operator);
			roleMenuDo.setRoleId(roleId);
			roleMenuDo.setRoleType(1);
			roleMenuDo.setMenuId(Long.parseLong(menuId));
			mgrRoleMenuDao.saveRecord(roleMenuDo);
		}

	}

	@Override
	public List<MgrRoleMenuDo> getCheckedMenu(Long roleId) {
		return mgrRoleMenuDao.getCheckedMenu(roleId);
	}
}