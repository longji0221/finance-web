package com.cx.finance.admin.web.controller;

import com.cx.finance.admin.spring.NotNeedLogin;
import com.cx.finance.admin.web.Sessions;
import com.cx.finance.admin.web.dto.req.RoleModReq;
import com.cx.finance.admin.web.dto.req.RoleQueryReq;
import com.cx.finance.admin.web.dto.resp.QueryRespData;
import com.cx.finance.admin.web.dto.resp.Resp;
import com.cx.finance.biz.service.*;
import com.cx.finance.common.annotation.Log;
import com.cx.finance.dal.dao.MgrOperatorRoleDao;
import com.cx.finance.dal.domain.*;
import com.cx.finance.dal.query.RoleQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping("/api/auth")
@ResponseBody
public class AuthController extends BaseController{


    @Resource
    private MgrMenuService mgrMenuService;

    @Resource
    private MgrRoleService mgrRoleService;

    @Resource
    private MgrOperatorService mgrOperatorService;

    @Resource
    private MgrRoleMenuService mgrRoleMenuService;

    @Resource
    private MgrOperatorRoleDao mgrOperatorRoleDao;


    @RequestMapping("/menuTree")
    public Resp<?> getMenuTree(String roleId){
        List<MgrMenuDo> menuDoList= mgrMenuService.getMenuByParentId(0L);
        for(MgrMenuDo menuDo:menuDoList){
            menuDo.setChildren(childrenMenu(menuDo));
        }
        Map<String,Object> data=new HashMap<>();
        data.put("menusList",menuDoList);
        List<MgrRoleMenuDo> menuChecked=mgrMenuService.getCheckedMenu(Long.parseLong(roleId));
        data.put("menuChecked",menuChecked);
        logger.info("menuChecked"+menuChecked.size());
        return Resp.succ(data,"");
    }

    private List<MgrMenuDo>  childrenMenu(MgrMenuDo menuDo){
        List<MgrMenuDo> menuDoList= mgrMenuService.getMenuByParentId(menuDo.getRid());
        if(menuDoList.size()==0){
            return null;
        }else {
            for(MgrMenuDo mgrMenuDo:menuDoList){
                mgrMenuDo.setChildren(this.childrenMenu(mgrMenuDo));;
            }
        }
        return menuDoList;
    }

    @Log("查询角色")
    @RequestMapping("/role")
    public Resp<?> getRole(@RequestBody @Valid RoleQueryReq req){
        RoleQuery query=req.toDalQuery();
        List<MgrRoleDo> roleList= mgrRoleService.getRoleList(query);
        return Resp.succ(QueryRespData.gen(roleList,query.getTotalCount()),"");
    }

    @RequestMapping("/divAuth")
    public Resp<?> getDivAuth(String menuUrl,HttpServletRequest request){
        String username = Sessions.getUsername(request);
        String divMenusId=null;
        if(Sessions.isLogin(request)) {
            MgrOperatorDo operatorDO = mgrOperatorService.getByUsername(username);
            divMenusId=mgrOperatorService.getDivMenusIdByOperatorId(operatorDO.getRid(),menuUrl);
        }
        return Resp.succ(divMenusId, null);
    }


    @RequestMapping("/set")
    public Resp<?> setAuth(String roleId,String menuIds,HttpServletRequest request){
        String username = Sessions.getUsername(request);
        mgrOperatorService.clearMenus(Long.parseLong(roleId));
        if(StringUtils.isNotBlank(menuIds)){
            mgrRoleMenuService.setAuthMenus(Sessions.getRealname(request),Long.parseLong(roleId),menuIds.substring(0,menuIds.length()-1));
        }
        return Resp.succ();
    }

    @Log("新增角色")
    @RequestMapping(value ="/addRole.json")
    public Resp<?> addRole( String roleName,String remark, HttpServletRequest request){
        MgrRoleDo roleDo=new MgrRoleDo();
        roleDo.setCreator(Sessions.getRealname(request));
        roleDo.setGmtCreate(new Date());
        roleDo.setStatus("0");
        roleDo.setRemark(remark);
        roleDo.setRoleName(roleName);
        roleDo.setRoleType(0);
        roleDo.setModifier(Sessions.getRealname(request));
        mgrRoleService.saveRecord(roleDo);
        return Resp.succ();
    }

    @Log("修改角色")
    @RequestMapping(value ="/updateRole.json")
    public Resp<?> updateRole(Long rid,String roleName,String remark, HttpServletRequest request){
        MgrRoleDo roleDo=new MgrRoleDo();
        roleDo.setRid(rid);
        roleDo.setRemark(remark);
        roleDo.setRoleName(roleName);
        roleDo.setModifier(Sessions.getRealname(request));
        roleDo.setGmtModified(new Date());
        mgrRoleService.updateById(roleDo);
        return Resp.succ();
    }


    @Log("修改状态")
    @RequestMapping(value ="/modStatus.json")
    public Resp<?> updateRole(Long rid,String status, HttpServletRequest request){
        MgrRoleDo roleDo =mgrRoleService.getById(rid);
        roleDo.setRid(rid);
        roleDo.setStatus(status);
        roleDo.setModifier(Sessions.getRealname(request));
        mgrRoleService.updateById(roleDo);
        return Resp.succ();
    }



    @RequestMapping("/getRole")
    public Resp<?> getRole(String operatorId,HttpServletRequest request){
        MgrOperatorRoleDo mgrOperatorRoleDo=null;
        Map<String,Object> data=new HashMap<>();
        if(StringUtils.isNotBlank(operatorId)){
            mgrOperatorRoleDo=mgrOperatorRoleDao.getByOperatorId(Long.valueOf(operatorId));
            data.put("roleId",mgrOperatorRoleDo.getRoleId());
        }
        List<MgrRoleDo> mgrRoleDos= mgrRoleService.getRole();
        data.put("roles",mgrRoleDos);
        return Resp.succ( data,"");
    }

}
