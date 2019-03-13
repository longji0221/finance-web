package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.MgrLogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.MgrLogDao;
import com.cx.finance.dal.domain.MgrLogDo;
import com.cx.finance.biz.service.MgrLogService;

import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-12-17 09:53:58
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("mgrLogService")
public class MgrLogServiceImpl extends ParentServiceImpl<MgrLogDo, Long> implements MgrLogService {
	
    private static final Logger logger = LoggerFactory.getLogger(MgrLogServiceImpl.class);
   
    @Resource
    private MgrLogDao mgrLogDao;

		@Override
	public BaseDao<MgrLogDo, Long> getDao() {
		return mgrLogDao;
	}

	@Autowired
	MgrLogDao logMapper;

	@Async
	@Override
	public void save(MgrLogDo logDO) {
		logMapper.save(logDO);
	}

	@Override
	public List<MgrLogDo> queryList(MgrLogQuery query) {
		return logMapper.queryList(query);
	}

	@Override
	public int remove(Long id) {
		int count = logMapper.remove(id);
		return count;
	}

	@Override
	public int batchRemove(Long[] ids){
		return logMapper.batchRemove(ids);
	}

	@Override
	public List<MgrLogDo> listByQueruy(MgrLogQuery query) {
		return logMapper.listByQueruy(query);
	}
}