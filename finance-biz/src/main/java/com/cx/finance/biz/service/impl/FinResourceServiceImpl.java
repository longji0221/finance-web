package com.cx.finance.biz.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cx.finance.dal.dao.FinResourceDao;
import com.cx.finance.dal.domain.FinResourceDo;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cx.finance.biz.service.FinResourceService;
import com.cx.finance.common.ConfigProperties;
import com.cx.finance.common.Constants;

import javax.annotation.Resource;

@Service("finResourceService")
public class FinResourceServiceImpl implements FinResourceService {

    protected final Logger logger = Logger.getLogger(getClass());

    @Resource
    private FinResourceDao finResourceDao;

	public Map<String, String> getApiUrl() {
        List<FinResourceDo> finResourceDos=finResourceDao.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,Constants.THIRD_PRODUCT);
        Map<String,String> dataUrl=new HashMap<>();
        for(FinResourceDo resource:finResourceDos){
            dataUrl.put(resource.getName(),resource.getValue());
        }
        return dataUrl;
	}
	
	

	public String getCompanySimpleNameByBenifitAccount(String benifitEmail) {
	   List<FinResourceDo> resourceDos=finResourceDao.getFinResourceListByTypeAndSecType(Constants.FINANCE_CONFIG,Constants.BENEFIT_ACCOUNT);
	   for(FinResourceDo resource:resourceDos) {
           if (benifitEmail.contains(resource.getValue())) {
               return resource.getName();
           }
       }
        return "未匹配商户";
	}

    @Override
    public List<FinResourceDo> getFinResourceListByTypeAndSecType(String type, String secType) {
        return finResourceDao.getFinResourceListByTypeAndSecType(type,secType);
    }

    @Override
    public FinResourceDo getFinResourceListByTypeAndSecTypeAndValue(String type, String SecType, String value) {
        return finResourceDao.getFinResourceListByTypeAndSecTypeAndValue(type,SecType, value);
    }
}
