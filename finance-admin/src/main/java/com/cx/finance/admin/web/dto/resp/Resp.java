package com.cx.finance.admin.web.dto.resp;

import com.cx.finance.admin.web.RespCode;

public class Resp<T> {
	public Integer code;
	public String msg;
	
	public T data;
	
	/**
	 * @param body @nullable
	 * @param failMsg @nullable
	 * @return
	 */
	public static <T> Resp<T> fail(T body, Integer code, String failMsg) {
		Resp<T> resp = new Resp<T>();
		resp.data = body;
		resp.code = code;
		resp.msg = failMsg;
		
		return resp;
	}
	/**
	 * @param failMsg
	 * @return
	 */
	public static <T> Resp<T> fail(String failMsg) {
		return fail(null, RespCode.FAIL.code, failMsg);
	}
	
	/**
	 * @param body @nullable
	 * @param succMsg @nullable
	 * @return
	 */
	public static <T> Resp<T> succ(T body, String succMsg) {
		Resp<T> resp = new Resp<T>();
		resp.data = body;
		resp.code = RespCode.SUCC.code;
		resp.msg = succMsg;
		
		return resp;
	}

	public static <T> Resp<T> succ(Integer code, String succMsg,String type) {
		Resp<T> resp = new Resp<T>();
		resp.code = code;
		resp.msg = succMsg;

		return resp;
	}
	public static <T> Resp<T> succ(){
		return succ(null, null);
	}
	
}