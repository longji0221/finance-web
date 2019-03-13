package com.cx.finance.admin.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cx.finance.admin.web.RespCode;
import com.cx.finance.admin.web.dto.resp.Resp;

/**
 * Controller异常统一处理器
 * 
 * @author ZJF, Create 2018年9月17日 
 */
@ControllerAdvice
@ResponseBody
public class ExecptionHandler{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @param exception 参数错误
	 * @param response
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Resp<?> dealBindException(BindException e, HttpServletResponse response){
		FieldError fieldError = e.getFieldError();
		logger.error("Request params error! Class = " + e.getTarget().getClass().getSimpleName() + ", Msg = " + fieldError.getField() + " is [" + fieldError.getRejectedValue() + "] " + fieldError.getCode());
		
		return Resp.fail(null, RespCode.PARAMS_ERROR.code, "请求参数不合法");
	}
	
	/**
	 * @param exception 参数错误
	 * @param response
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Resp<?> dealArgsException(MethodArgumentNotValidException e, HttpServletResponse response){
		BindingResult br = e.getBindingResult();
		FieldError fieldError = br.getFieldError();
		logger.error("Request argument error! Class=" + br.getTarget().getClass().getSimpleName() + ", Msg = " + fieldError.getField() + " is [" + fieldError.getRejectedValue() + "] " + fieldError.getCode());
		return Resp.fail(null, RespCode.PARAMS_ERROR.code, "请求参数不合法");
	}
	
   /**
    * @param exception UnexpectedTypeException
    * @param response
    * @return
    */
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   @ExceptionHandler(Throwable.class)
   public Resp<?> dealAllException(Throwable e, HttpServletResponse response){
	   logger.error("System Exception! Msg=" + e.getMessage(), e);
	   return Resp.fail(null, RespCode.SYS_ERROR.code, "系统开小差了，请联系管理员");
   }
	
}
