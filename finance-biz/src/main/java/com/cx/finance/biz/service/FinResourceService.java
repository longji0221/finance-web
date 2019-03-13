package com.cx.finance.biz.service;

import com.cx.finance.dal.domain.FinResourceDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FinResourceService {
	
	Map<String, String> getApiUrl();
	
	String getCompanySimpleNameByBenifitAccount(String benifitAccount);

	List<FinResourceDo> getFinResourceListByTypeAndSecType(String type,String secType);

	FinResourceDo getFinResourceListByTypeAndSecTypeAndValue(String type, String SecType,String value);
}
