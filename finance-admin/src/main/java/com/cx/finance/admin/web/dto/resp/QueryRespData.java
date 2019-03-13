package com.cx.finance.admin.web.dto.resp;

import java.util.List;

public class QueryRespData<T> {
	
	public List<T> list;
	public int totalCount;
	
	public static <T> QueryRespData<T> gen(List<T> list, int totalCount) {
		QueryRespData<T> respData = new QueryRespData<T>();
		respData.list = list;
		respData.totalCount = totalCount;
		return respData;
	}
}
