package com.cx.finance.admin.aspect;

import com.cx.finance.admin.web.Sessions;
import com.cx.finance.biz.service.MgrLogService;
import com.cx.finance.common.annotation.Log;
import com.cx.finance.common.util.HttpUtil;
import com.cx.finance.common.util.IPUtils;
import com.cx.finance.common.util.JSONUtils;
import com.cx.finance.dal.domain.MgrLogDo;
import com.cx.finance.dal.domain.MgrOperatorDo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    MgrLogService logService;


    @Pointcut("@annotation(com.cx.finance.common.annotation.Log)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //异步保存日志
        try {
            saveLog(point, time);
        } catch (Exception e) {
            logger.error("logPointCut is Error, e = "+ e);
        }
        return result;
    }

    void saveLog(ProceedingJoinPoint joinPoint, long time) throws InterruptedException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MgrLogDo sysLog = new MgrLogDo();
        Log syslog = method.getAnnotation(Log.class);
        if (syslog != null) {
            // 注解上的描述
            sysLog.setOperation(syslog.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JSONUtils.beanToJson(args[0]);
            System.out.println(params);
            sysLog.setParams(params);
        } catch (Exception e) {
            sysLog.setParams("Exception");
            logger.info("params is Exception ,e= "+e );
            System.out.println(e);
        }
        // 获取request
        HttpServletRequest request = HttpUtil.getHttpServletRequest();
        // 设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        // 用户名

        MgrOperatorDo currUser = Sessions.getUserMo(request);
        if (sysLog.getParams().contains("username") && sysLog.getParams().contains("passwd")) {
            if (null != sysLog.getParams()) {
                sysLog.setUserId(-1L);
                sysLog.setParams("{\"passwd\":\"******\",\"username\":\""+sysLog.getParams().substring(sysLog.getParams().length()-13,sysLog.getParams().length()-2)+"\"}");//登录参数隐藏用户密码信息
                sysLog.setUsername(sysLog.getParams().substring(sysLog.getParams().length()-13,sysLog.getParams().length()-2));
            } else {
                sysLog.setUserId(-1L);
                sysLog.setUsername("获取用户信息为空");
            }
        } else {
            sysLog.setUserId(currUser.getRid());
            sysLog.setUsername(currUser.getUserName());
        }
        sysLog.setTime((int) time);
        // 系统当前时间
        Date date = new Date();
        sysLog.setGmtCreate(date);
        // 保存系统日志
        logService.save(sysLog);
    }
}
