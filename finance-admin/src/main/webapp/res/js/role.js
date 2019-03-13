/**
  宝贝违禁词功能JS
**/ 
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
	UI.init();
	
	exports('role', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
	init : function(){
		/* START ---- 列表初始化 */
		layui.table.render({
		  elem: '#roleTable',
		  cellMinWidth: 80,
		  height: 470,
		  page: true,
		  loading: false,
		  url: '/api/auth/role.json',
		  contentType: 'application/json; charset=utf-8',
		  method: 'post',
		  parseData: function(res){ //res 即为原始返回的数据
		    return {
		      "code": res.code, //解析接口状态
		      "msg": res.msg, //解析提示文本
		      "count": res.data.totalCount, //解析数据长度
		      "data": res.data.list //解析数据列表
		    };
		  },
		  response: { statusCode: 100 },
	      cols: [[ //表头
			  {width:300, field:'roleName', align:'center', unresize: true, title: "角色名称"},
              {width:300, field:'modifier', align:'center', unresize: true, title: "修改人"},
              {width:300, field:'gmtCreate', align:'center', unresize: true, title: "创建时间"},
              {width:300, field:'remark', align:'center', unresize: true, title: "角色描述"},
              {width:200, field:'status', templet:'#dlStatus', align:'center', unresize: true, title: "状态"},
			  {width:350, templet:'#dlOpt',align :'center', unresize: true, title: "操作",event	:'none', style:'cursor: pointer;'}
		  ]]
		});
		/* END ---- 列表初始化 */
		
		
		/* START ---- 日期组件初始化 */
		layui.laydate.render({
		  elem: '#dateStart'
		  ,max: 1
		  ,type: 'datetime'
		  ,theme: '#01AAED'
		});
		layui.laydate.render({
		  elem: '#dateEnd'
		  ,max: 1
		  ,type: 'datetime'
		  ,theme: '#01AAED'
		});
		/* END ---- 日期组件初始化 */
		
		
		/* START ---- 数据上送初始化 */
		var form = layui.form;
	    form.on('submit(sbtnCreate)', function(laydata){
            $.ajax({
                url:"/api/auth/addRole.json",
                data:{roleName:$("#addRoleName").val(),remark:$("#remark").val()},
                success:function(result) {
                    if(result.code == 100){
                        UI.tipSucc('添加成功！', function(){
                            UI.query();
                        });
                    }else{
                        UI.tipErr(resp.msg);
                    }
                },
                error:function () {
                    layer.msg("请求异常")
                }
            })
	    	return false;
	    });
	    form.on('submit(sbtnModify)', function(laydata){
            $.ajax({
                url:"/api/auth/updateRole.json",
                data:{roleName:$("#updateRoleName").val(),
                      remark:$("#updateRemark").val(),
                      rid:$("#updateRid").val()},
                success:function(result) {
                    if(result.code == 100){
                        UI.tipSucc('添加成功！', function(){
                            UI.query();
                        });
                    }else{
                        UI.tipErr(resp.msg);
                    }
                },
                error:function () {
                    layer.msg("请求异常")
                }
            })
	    	return false;
	    });
	    /* END ---- 数据上送初始化 */
	    
		
	    /* START --- 事件监听区域  ----- */
		$('#btnSearch').click(function(){
			UI.query();
			return false;
		});
		
		$('#btnClear').click(function(){
			$('#formQuery')[0].reset();
			return false;
		});

		
		$('#btnExport').click(function(){
			// TODO
			return false;
		});

        $('#allCheck').click(function(){
            var treeObj = $.fn.zTree.getZTreeObj('authTree');
            treeObj.checkAllNodes(true);
            return;
        });
        $('#allNoCheck').click(function(){
            var treeObj = $.fn.zTree.getZTreeObj('authTree');
            treeObj.checkAllNodes(false);
            return;
        });
        $('#allOpen').click(function(){
            var treeObj = $.fn.zTree.getZTreeObj('authTree');
            treeObj.expandAll(true);
            return;
        });
        $('#allClose').click(function(){
            var treeObj = $.fn.zTree.getZTreeObj('authTree');
            treeObj.expandAll(false);
            return;
        });
		/* END --- 事件监听区域  ----- */
	},

    auth:function(row){
        // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
        var roleId = $(row).attr('data-id');
        // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
        $.ajax({
            url:'/api/auth/menuTree.json', data:{roleId:roleId}, success:function(result){
                var zNodes=result.data.menusList;
                var checkedNodes=result.data.menuChecked;
                var setting = {
                    treeId:"treeDemo",
                    check: {
                        enable: true,   //true / false 分别表示 显示 / 不显示 复选框或单选框
                        chkStyle: "checkbox",   //勾选框类型(checkbox 或 radio）
                        chkboxType: { "Y": "ps", "N": "ps" }   //勾选 checkbox 对于父子节点的关联关系
                    },
                    data:{
                        key:{
                            name:"menuName"
                        },
                        simpleData:{
                            enable:true,
                            idKey: "rid",
                            pIdKey: "parentId",
                            rootPId: 0
                        }
                    },
                    callback:{

                    }
                };
                var  zTreeObj = $.fn.zTree.init($("#authTree"), setting, zNodes);
                for (var i=0; i<checkedNodes.length;  i++) {
                    var node = zTreeObj.getNodeByParam("rid", checkedNodes[i].rid);//根据ID找到该节点
                    if(node!=null){
                        zTreeObj.checkNode(node, true, false);
                    }
                }
                if(result.code==100){
                    UI.openTree(zTreeObj,roleId);
                }else {
                    alert("请求菜单树异常")
                }
            }
        });
    },

    openTree:function(zTreeObj,roleId){
        var flag=false;
        layer.open({
            id:"auth",
            type: 1,
            title: "权限配置",
            area:  [ "500px",
                "480px" ],
            btnAlign: 'c',
            btn:["取消","确定"],
            content: $("#menuAuth"),
            cancel:function(){

            },
            btn2:function(index){
                if(flag){
                    return false;
                }
                flag=true;
                var nodes = zTreeObj.getCheckedNodes(true);
                var menuIds='';
                for(i=0;i<nodes.length;i++){
                    menuIds=menuIds+nodes[i].rid+',';
                }
                $.ajax({
                    url:'/api/auth/set.json', data:{roleId:roleId,menuIds:menuIds}, success:function(data){
                        if(data.code==100){
                            layer.alert("设置成功!");
                        }else {
                            layer.alert("权限设置失败!")
                        }
                        layer.close(index)
                    }
                });
                return false;
            }
        })
    },
	/**
	 * 列表查询函数
	 */
	query: function(){
        API.getLoginStatus(function(jsonResp){
            var isLogin = jsonResp.data.isLogin;
            var username = jsonResp.data.username;
            var menuIds = jsonResp.data.menuIds;
            if(isLogin == true){
                $('#username').text(username);
                var menuIdArrs = menuIds.split(',');
                for(var i = 0; i < menuIdArrs.length; i++ ){
                    $('#divNavLeft a[data-id='+ menuIdArrs[i] +']').parent().show();
                }
            }else{
                layer.alert('<span style="font-size:18px;">会话超时，需要重新登录!</span>', {icon:6, area:['360px','180px'], btn:['确定']},
                    function(){
                        layer.closeAll();
                        window.parent.location.href= '/login.html';
                    });
            }
        });
		layui.table.reload('roleTable', {
	        page: {
	          curr: 1 //重新从第 1 页开始
	        },
	        where: {
                roleName: $('#roleName').val(),
	          status: $('#status').val(),
	          dateStart: $('#dateStart').val(),
	          dateEnd: $('#dateEnd').val()
	        }
		});
	},



    addRole:function(){
        $("#addRoleName").val(""),
        $("#remark").val("")
        layer.open({
            type: 1,
            title: "新增角色",
            closeBtn: 1,
            maxWidth:470,
            shadeClose: false,
            content:$("#divCreate")
        });
    },

	/**
	 * 弹框-修改管理员
	 */
	showModifyPopup: function(srcDom){
		var $divMod = $('#divModify');
		var dataObj = JSON.parse($(srcDom).attr('data-entity'));
		Tool.autoInpourData2Html(dataObj, $divMod[0]);
		layer.open({
		  type: 1,
		  title: "修改角色",
		  closeBtn: 1,
		  area: 'auto',
		  maxWidth: 550,
		  shadeClose: false,
		  content: $divMod
		});
	},


    thawing:function(srcDom) {
        layer.alert('<span style="font-size:18px;">确定禁用角色？</span>', {icon:6, area:['360px','180px'], btn:['确定']},
            function(){
                var id = $(srcDom).attr('data-id');
                $.ajax({
                    url:'/api/auth/modStatus.json', data:{"rid": id, "status": 1}, success:function(data){
                        if (data.code == 100) {
                            UI.tipSucc('禁用成功！', function () {
                                UI.query();
                            });
                        } else {
                            UI.tipErr(data.msg);
                        }
                        layer.close(index)
                    }
                });
            });


    },

    freeze:function(srcDom) {

        layer.alert('<span style="font-size:18px;">确定解禁角色？</span>', {icon:6, area:['360px','180px'], btn:['确定']},
            function(){
                var id = $(srcDom).attr('data-id');
                $.ajax({
                    url:'/api/auth/modStatus.json', data:{"rid": id, "status": 0}, success:function(data){
                        if (data.code == 100) {
                            UI.tipSucc('解禁成功！', function () {
                                UI.query();
                            });
                        } else {
                            UI.tipErr(data.msg);
                        }
                        layer.close(index)
                    }
                });
            });



    },





	/**
	 * 根据status code 获取描述
	 */
	getStatusDesc : function(code){
		if(code == '1'){
			return '正常';
		}else if(code == '2'){
			return '冻结';
		}else if(code == '3'){
			return '离职';
		}
	},
	
	/**
	 * 弹框 - 成功提示
	 */
	tipSucc : function(msg, callback){
		layer.closeAll();
		layer.msg(msg, {
			icon: 1,
			time: 2000 //2秒关闭
		}, callback);
	},
	
	/**
	 * 弹框 - 错误提醒
	 */
	tipErr : function(msg){
		layer.msg(msg, {icon: 5, offset: '70px', time:3000});
	}
};

/**
 * ------------- 与服务端通信模块 --------------
 */
var API = {
    getLoginStatus: function(callback){
        $.post("/api/login/status.json", function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
    del : function(data, callback){
        $.post('/api/operator/del.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
    editStatus : function(data, callback){
        $.post('/api/operator/modStatus.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
    edit : function(data, callback){
        $.post('/api/auth/addRole.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },


}
