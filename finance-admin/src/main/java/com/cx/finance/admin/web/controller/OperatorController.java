package com.cx.finance.admin.web.controller;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cx.finance.admin.web.dto.req.ModPwdByMgrReq;
import com.cx.finance.dal.domain.MgrOperatorDto;
import com.cx.finance.common.annotation.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cx.finance.admin.web.LocalConstants;
import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.req.ModPwdReq;
import com.cx.finance.admin.web.dto.req.OperatorEditReq;
import com.cx.finance.admin.web.dto.req.OperatorQueryReq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.common.Constants;
import com.cx.finance.common.util.DigestUtil;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.dao.MgrOperatorDao;
import com.cx.finance.dal.dao.MgrOperatorRoleDao;
import com.cx.finance.dal.domain.MgrOperatorDo;
import com.cx.finance.dal.domain.MgrOperatorRoleDo;
import com.cx.finance.dal.query.OperatorQuery;

@Controller
@ResponseBody
@RequestMapping("/api/operator")
public class OperatorController extends BaseController {
	
	@Resource
	private MgrOperatorDao mgrOperatorDao;
	@Resource
	private MgrOperatorRoleDao mgrOperatorRoleDao;

	@Log("查询用户列表")
    @RequestMapping(value = "/list.json")
    public Resp<?> list(@RequestBody @Valid OperatorQueryReq req, HttpServletRequest request) {
    	OperatorQuery dalQuery = req.toDalQuery();
    	List<MgrOperatorDto> res = mgrOperatorDao.pageQuery(dalQuery);
        return Resp.succ(QueryRespData.gen(res, dalQuery.getTotalCount()), null);
    }

    @Log("编辑用户")
    @RequestMapping(value = "/edit.json")
    public Resp<?> edit(@RequestBody @Valid OperatorEditReq params, HttpServletRequest request) {
    	String operator = Sessions.getRealname(request);
        MgrOperatorDo operatorDo = null;
        MgrOperatorRoleDo operatorRoleDo = null;
        if(params.rid != null){	//edit
        	operatorDo = mgrOperatorDao.getById(params.rid);
        	if(operatorDo == null) return Resp.fail("用户不存在");
        }else{					//add
        	operatorDo = mgrOperatorDao.getByUsername(params.mobile);
            if (null != operatorDo) return Resp.fail("手机号已存在");
            operatorDo = new MgrOperatorDo();
            operatorDo.setCreator(operator);
            operatorDo.setStatus(1);
            operatorDo.setUserName(params.mobile);
            operatorDo.setMobile(params.mobile);

            operatorRoleDo = new MgrOperatorRoleDo();
            operatorRoleDo.setCreator(operator);
            operatorRoleDo.setModifier(operator);
            operatorRoleDo.setRoleType(1);
            operatorRoleDo.setRoleId(params.roleId);
        }
        
        if(StringUtil.isNotBlank(params.password)) {
        	this.setPassword(operatorDo, params.password);
        }
        operatorDo.setModifier(operator);
        operatorDo.setRealName(params.realName);
        operatorDo.setEmail(params.email);
        
        if (params.rid != null) {//edit
        	mgrOperatorDao.updateById(operatorDo);
        	logger.info("do update operator success with operatorDo=" + operatorDo);
        }else{					//add
        	mgrOperatorDao.saveRecord(operatorDo);
        	operatorRoleDo.setOperatorId(operatorDo.getRid());
        	mgrOperatorRoleDao.saveRecord(operatorRoleDo);
        	logger.info("do add operator success with operatorDo=" + operatorDo);
        }
        
        return Resp.succ();
    }

    @Log("修改用户账户状态")
    @RequestMapping(value = "/modStatus.json")
    public Resp<?> modStatus(@RequestBody  OperatorEditReq params, HttpServletRequest request){
       Long id=params.rid;
       MgrOperatorDo operatorDo=new MgrOperatorDo();
       operatorDo.setRid(id);
       operatorDo.setStatus(params.status);
        operatorDo.setModifier(Sessions.getRealname(request));
        mgrOperatorDao.updateById(operatorDo);
        return Resp.succ();
    }

    @Log("删除用户")
    @RequestMapping(value = "/del.json")
    public Resp<?> del(@RequestBody  OperatorEditReq params, HttpServletRequest request){
        Long id=params.rid;
        MgrOperatorDo operatorDo=new MgrOperatorDo();
        operatorDo.setRid(id);
        operatorDo.setIsDelete(1);
        mgrOperatorDao.updateById(operatorDo);
        return Resp.succ();
    }

    /**
     * 用户修改密码
     * @return
     */
    @Log("用户修改密码")
    @RequestMapping(value = "/modPwd.json")
    public Resp<?> modPwd(@RequestBody @Valid ModPwdReq params, HttpServletRequest request){
    	Long uid = Sessions.getUid(request);
    	if(params.newPasswd.length()<6){
            return Resp.fail("密码过短(最小6位数)！");
        }
    	if(StringUtil.isBlank(params.oriPasswd) || StringUtil.isBlank(params.newPasswd)){
            return Resp.fail("新老密码不能为空！");
        }
    	MgrOperatorDo optDo = mgrOperatorDao.getById(uid);
        byte[] saltBytes = DigestUtil.decodeHex(optDo.getSalt());
        
        byte[] oriPwdBytes = DigestUtil.digestString(params.oriPasswd.getBytes(LocalConstants.UTF_8), saltBytes, Constants.DEFAULT_DIGEST_TIMES, Constants.SHA1);
        String oriPwd = DigestUtil.encodeHex(oriPwdBytes);
        
        String dbPwd = optDo.getPassword();
        if (!StringUtils.equals(oriPwd, dbPwd)) {
        	return Resp.fail("原密码错误！");
        }
        
        byte[] newPwdBytes = DigestUtil.digestString(params.newPasswd.getBytes(LocalConstants.UTF_8), saltBytes, Constants.DEFAULT_DIGEST_TIMES, Constants.SHA1);
        String newPwd = DigestUtil.encodeHex(newPwdBytes);

        MgrOperatorDo mgrOperatorDo=new MgrOperatorDo();
        mgrOperatorDo.setRid(optDo.getRid());
        mgrOperatorDo.setPassword(newPwd);
        mgrOperatorDao.updateById(mgrOperatorDo);
        
        return Resp.succ();
    }

    /**
     * 管理员修改密码
     * @return
     */
    @Log("管理员修改密码")
    @RequestMapping(value = "/modPwdByMgr.json")
    public Resp<?> modPwdByMgr(@RequestBody @Valid ModPwdByMgrReq params, HttpServletRequest request){
        Long uid =params.rid;

        MgrOperatorDo optDo = mgrOperatorDao.getById(uid);
        byte[] saltBytes = DigestUtil.decodeHex(optDo.getSalt());

        byte[] newPwdBytes = DigestUtil.digestString(params.newPasswd.getBytes(LocalConstants.UTF_8), saltBytes, Constants.DEFAULT_DIGEST_TIMES, Constants.SHA1);
        String newPwd = DigestUtil.encodeHex(newPwdBytes);

        MgrOperatorDo mgrOperatorDo=new MgrOperatorDo();
        mgrOperatorDo.setRid(uid);
        if(StringUtil.isNotBlank(params.newPasswd)){
            if(params.newPasswd.length()<6){
                return Resp.fail("密码过短(最小6位数)！");
            }
            mgrOperatorDo.setPassword(newPwd);
        }
        mgrOperatorDo.setRealName(params.realName);
        mgrOperatorDao.updateById(mgrOperatorDo);
        MgrOperatorRoleDo mgrOperatorRoleDo=mgrOperatorRoleDao.getByOperatorId(params.rid);
        if(mgrOperatorRoleDo==null){
            MgrOperatorRoleDo newOperatorRole=new MgrOperatorRoleDo();
            newOperatorRole.setCreator(Sessions.getRealname(request));
            newOperatorRole.setModifier(Sessions.getRealname(request));
            newOperatorRole.setGmtCreate(new Date());
            newOperatorRole.setRoleId(Long.valueOf(params.roleId));
            newOperatorRole.setOperatorId(params.rid);
            newOperatorRole.setRoleType(1);
            mgrOperatorRoleDao.saveRecord(newOperatorRole);
        }else {
            mgrOperatorRoleDo.setRoleId(Long.valueOf(params.roleId));
            mgrOperatorRoleDo.setGmtModified(new Date());
            mgrOperatorRoleDao.updateById(mgrOperatorRoleDo);
        }
        return Resp.succ();
    }

    private void setPassword(MgrOperatorDo operatorDo, String passwd){
    	String pwd = LocalConstants.DEFAULT_PASSWD;
    	if (operatorDo.getRid() != null) {	//edit
    	}else{								//add
    		if(StringUtil.isNotBlank(passwd)) {
    			pwd = passwd;
    		}
            byte[] saltBytes = DigestUtil.generateSalt(8);
            String salt = DigestUtil.encodeHex(saltBytes);
            byte[] pwdBytes = DigestUtil.digestString(pwd.getBytes(Charset.forName("UTF-8")), saltBytes, Constants.DEFAULT_DIGEST_TIMES, Constants.SHA1);
            operatorDo.setPassword(DigestUtil.encodeHex(pwdBytes));
            operatorDo.setSalt(salt);
    	}
    }
    
}
