package com.cx.finance.dal.domain;

import com.cx.finance.common.AbstractSerial;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-12-17 09:52:41
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class MgrLogDo extends AbstractSerial {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Rid
     */
    private Long rid;
    
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作
     */
    private String operation;

    /**
     * 响应时间
     */
    private Integer time;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;


    /**
     * 获取主键Id
     *
     * @return rid
     */
    public Long getRid(){
      return rid;
    }

    /**
     * 设置主键Id
     * 
     * @param
     */
    public void setRid(Long rid){
      this.rid = rid;
    }
    
    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public Long getUserId(){
      return userId;
    }

    /**
     * 设置用户id
     * 
     * @param userId 要设置的用户id
     */
    public void setUserId(Long userId){
      this.userId = userId;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername(){
      return username;
    }

    /**
     * 设置用户名
     * 
     * @param username 要设置的用户名
     */
    public void setUsername(String username){
      this.username = username;
    }

    /**
     * 获取用户操作
     *
     * @return 用户操作
     */
    public String getOperation(){
      return operation;
    }

    /**
     * 设置用户操作
     * 
     * @param operation 要设置的用户操作
     */
    public void setOperation(String operation){
      this.operation = operation;
    }

    /**
     * 获取响应时间
     *
     * @return 响应时间
     */
    public Integer getTime(){
      return time;
    }

    /**
     * 设置响应时间
     * 
     * @param time 要设置的响应时间
     */
    public void setTime(Integer time){
      this.time = time;
    }

    /**
     * 获取请求方法
     *
     * @return 请求方法
     */
    public String getMethod(){
      return method;
    }

    /**
     * 设置请求方法
     * 
     * @param method 要设置的请求方法
     */
    public void setMethod(String method){
      this.method = method;
    }

    /**
     * 获取请求参数
     *
     * @return 请求参数
     */
    public String getParams(){
      return params;
    }

    /**
     * 设置请求参数
     * 
     * @param params 要设置的请求参数
     */
    public void setParams(String params){
      this.params = params;
    }

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    public String getIp(){
      return ip;
    }

    /**
     * 设置IP地址
     * 
     * @param ip 要设置的IP地址
     */
    public void setIp(String ip){
      this.ip = ip;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public Date getGmtCreate(){
      return gmtCreate;
    }

    /**
     * 设置创建时间
     * 
     * @param gmtCreate 要设置的创建时间
     */
    public void setGmtCreate(Date gmtCreate){
      this.gmtCreate = gmtCreate;
    }




    public String toString() {
        return "MgrLogDO{" +
                "id=" + rid +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", operation='" + operation + '\'' +
                ", time=" + time +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", ip='" + ip + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }
}