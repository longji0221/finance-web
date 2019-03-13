package com.cx.finance.admin.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.cx.finance.common.annotation.Log;
import com.cx.finance.common.util.HttpUtil;
import com.cx.finance.common.util.StringUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cx.finance.admin.spring.NotNeedLogin;
import com.cx.finance.admin.web.LocalConstants;
import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.req.LoginReq;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.MgrRoleService;
import com.cx.finance.biz.util.BizCacheUtil;
import com.cx.finance.common.ConfigProperties;
import com.cx.finance.common.Constants;
import com.cx.finance.common.util.CommonUtil;
import com.cx.finance.common.util.DigestUtil;
import com.cx.finance.dal.dao.MgrOperatorDao;
import com.cx.finance.dal.domain.MgrOperatorDo;

import static com.alibaba.fastjson.JSON.toJSONString;

@Controller
@ResponseBody
@RequestMapping("/api/login")
public class LoginController extends BaseController {

	@Resource
	private MgrOperatorDao mgrOperatorDao;
    @Resource
    private MgrRoleService mgrRoleService;
    @Resource
    private BizCacheUtil bizCacheUtil;
    
    /**
     * 用户密码登陆
     * @param request
     * @param
     * @return
     */
    @Log("登录")
    @NotNeedLogin
    @RequestMapping(value = "/in.json", method = RequestMethod.POST)
    public Resp<?> doLogin(@RequestBody @Valid LoginReq loginReq, HttpServletRequest request) {
        MgrOperatorDo userDO = mgrOperatorDao.getByUsername(loginReq.username);
        if (null == userDO) {
            return Resp.fail("用户不存在");
        }
        if(userDO.getStatus()==2){
            return Resp.fail("用户被冻结，请联系管理员解冻");
        }
        if(userDO.getStatus()==3){
            return Resp.fail("用户已离职，不能登录");
        }
        byte[] saltBytes = DigestUtil.decodeHex(userDO.getSalt());
        byte[] reqPwdBytes = DigestUtil.digestString(loginReq.passwd.trim().getBytes(LocalConstants.UTF_8), saltBytes, Constants.DEFAULT_DIGEST_TIMES, Constants.SHA1);
        String reqPwd = DigestUtil.encodeHex(reqPwdBytes);
        String dbPwd = userDO.getPassword().trim();
        if (!StringUtils.equals(reqPwd, dbPwd)) {
        	return Resp.fail("密码错误");
        }
        
        Sessions.setUserMo(request, userDO);
        return Resp.succ();
    }

    /**
     * 短信验证码登录
     *
     * @param request
     * @param
     * @return
     * @throws Exception
     */
    @Log("短信验证码登录")
    @NotNeedLogin
    @RequestMapping(value = "/smsIn.json", method = RequestMethod.POST)
    public Resp<?> doSmsLogin(@RequestBody @Valid LoginReq loginReq, HttpServletRequest request) {
        MgrOperatorDo userDO = mgrOperatorDao.getByUsername(loginReq.username);
        if (null == userDO) {
            return Resp.fail("用户不存在");
        }
        
    	String cacheKey = LocalConstants.CACHE_KEY_LOGIN_SMS + loginReq.username;
        String smsObj = bizCacheUtil.get(cacheKey);

        if (smsObj == null){
            return Resp.fail("请先获取验证码");
        }
        if (!StringUtils.equals(loginReq.verifyCode, smsObj)) {
        	return Resp.fail("验证码错误");
        }
        bizCacheUtil.delCache(cacheKey);
        
        Sessions.setUserMo(request, userDO);
        return Resp.succ();
    }
    /**
     * 获取验证码
     * @return
     */
    @Log("获取验证码")
    @NotNeedLogin
    @RequestMapping(value = "/sendSms.json", method = RequestMethod.POST)
    public Resp<?> verifyCode(@RequestBody @Valid LoginReq loginReq){

        try {
            MgrOperatorDo userDO = mgrOperatorDao.getByUsername(loginReq.username);

            if (null == userDO) {
                return Resp.fail(null, 900, "用户不存在");
            }
            String cacheKey = LocalConstants.CACHE_KEY_LOGIN_SMS + loginReq.username;
            String smsObj = bizCacheUtil.get(cacheKey);
            if (smsObj != null){
                return Resp.fail("验证码五分钟内有效");
            }
            String verifyCode = CommonUtil.getRandomNumber(6);

            //调用爱上街发短信接口
            String salt = "e5f8faa9f3ef837e";
            byte[] saltBytes = DigestUtil.decodeHex(salt);
            byte[] curPwdBytes = new byte[0];
        try {
            curPwdBytes = DigestUtil.digestString(loginReq.username.getBytes("UTF-8"), saltBytes,
                        Constants.DEFAULT_DIGEST_TIMES, Constants.SHA1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String enmobile = DigestUtil.encodeHex(curPwdBytes);

            Map<String,String> info = new HashMap<>();
            info.put("mobile",userDO.getUserName());
            String content = Constants.SMS_LOGIN.replace("&params", verifyCode);
            info.put("message",content);
            info.put("enmobile",enmobile);

            long startTimeMillis = System.currentTimeMillis();
            logger.info("startTimeMillis is "+startTimeMillis);
            String reqResult = HttpUtil.post(getIsjAdminUrl() + "/sms/riskSmsApi.htm", info);
            long endTimeMillis = System.currentTimeMillis();
            logger.info("endTimeMillis is "+endTimeMillis);
            logger.info("reqResult is {}"+reqResult);
            if("SUCCESS".equals(reqResult)){
                bizCacheUtil.delCache(cacheKey);
                bizCacheUtil.setTime(cacheKey, verifyCode, LocalConstants.CACHE_KEY_LOGIN_SMS_EXPIRE_SECS);
                logger.info(userDO.getUserName()+"verifyCode is "+verifyCode);
            } else {
                return Resp.fail("发送失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("mobile = "+loginReq.username +"e ="+e);
            return Resp.fail("系统异常");
        }
        return Resp.succ();
    }
    
    /**
	 * 获取登录信息
	 */
    @Log("获取登录信息")
    @NotNeedLogin
	@RequestMapping(value = "/status.json")
	public Resp<?> status(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<>(4);
		data.put("isLogin", Sessions.isLogin(request));

		String username = Sessions.getUsername(request);
		data.put("username", username != null && username.length() >= 11? StringUtils.overlay(username, "****", 3, 7): username);

        if(Sessions.isLogin(request)) {
            MgrOperatorDo operatorDO = mgrOperatorDao.getByUsername(username);
            data.put("menuIds", mgrOperatorDao.getMenuIdsByOperatorId(operatorDO.getRid()));
        }
        return Resp.succ(data, null);
	}
    
    /**
     * 注销
     * @return
     */
    @Log("注销")
    @RequestMapping(value = "/out.json")
    public Resp<?> logout(HttpServletRequest request){
    	Sessions.empty(request);
        return Resp.succ();
    }

    private static String urlIsgAdmin = null;

    private static String getIsjAdminUrl() {
        if (urlIsgAdmin == null) {
            urlIsgAdmin = ConfigProperties.get(Constants.ISG_ADMIN_HOST);
            return urlIsgAdmin;
        }
        return urlIsgAdmin;
    }

}
