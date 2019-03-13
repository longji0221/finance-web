/**
  宝贝违禁词功能JS
**/ 
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
	UI.init();
	
	exports('operator', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
	init : function(){
		/* START ---- 列表初始化 */
		layui.table.render({
		  elem: '#demoTable',
		  cellMinWidth: 80,
		  height: 470,
		  page: true,
		  loading: false,
		  url: '/api/operator/list.json',
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
			  {width:150, field:'realName', align:'center', unresize: true, title: "姓名"},
              {width:170, field:'roleName', align:'center', unresize: true, title: "角色"},
              {width:210, field:'mobile', align:'center', unresize: true, title: "手机号"},
			  {width:100, field:'status', templet:'#dlStatus', align:'center', unresize: true, title: "账号状态"},
			  {width:220, field:'gmtCreate', templet:'#dlGmtCreate', align:'center', unresize: true, title: "创建时间"},
			  {width:110, field:'creator', align:'center', unresize: true, title: "创建者"},
              {width:210, field:'gmtModified', align:'center', unresize: true, title: "修改时间"},
              {width:300, field:'remark', align:'center', unresize: true, title: "角色描述"},
              {width:210, templet:'#dlOpt', align:'center', unresize: true, title: "操作",event	:'none', style:'cursor: pointer;'}
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
	    	var reqData = laydata.field;
	    	API.edit(JSON.stringify(reqData), function(resp){
	    		if(resp.code == 100){
    			  UI.tipSucc('添加成功！', function(){
    				  UI.query();
    			  });
	    		}else{
    			  UI.tipErr(resp.msg);
	    		}
	    	});
	    	
	    	return false;
	    });
	    form.on('submit(sbtnModify)', function(laydata){
	    	var reqData = laydata.field;
	    	API.modPwdByMgr(JSON.stringify(reqData), function(resp){
	    		if(resp.code == 100){
    			  UI.tipSucc('修改成功！', function(){
    				  UI.query();
    			  });
	    		}else{
    			  UI.tipErr(resp.msg);
	    		}
	    	});
	    	
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



		/* END --- 事件监听区域  ----- */
	},


	addOperator:function(){
		var form=layui.form;
        $("#roleSelect").html("");
        $.ajax({
            url:'/api/auth/getRole.json', success:function(data){
                if(data.code==100){
                    var roles=data.data.roles;
                    var html='';
                    for(var i=0;i<roles.length;i++){
                        html+="<option value='"+roles[i].rid+"'>"+roles[i].roleName+"</option>";
					}
					$("#roleSelect").append(html);
                    form.render('select')
                }else {
                    layer.alert("角色获取失败!")
                }
                layer.open({
                    type: 1,
                    title: "新增管理员",
                    closeBtn: 1,
                    maxWidth:500,
                    shadeClose: false,
                    content:$("#divCreate")
                });
            }
        });

        $('#passwordInput').on('focus',function () {
            document.getElementById('passwordInput').type="password";
        });
        return false;
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
		layui.table.reload('demoTable', {
	        page: {
	          curr: 1 //重新从第 1 页开始
	        },
	        where: {
	          realName: $('#realName').val(),
	          mobile: $('#mobile').val(),
	          status: $('#status').val(),
	          dateStart: $('#dateStart').val(),
	          dateEnd: $('#dateEnd').val()
	        }
		});
	},
	
	/**
	 * 弹框-修改管理员
	 */
	showModifyPopup: function(srcDom){
        $("#uRoleSelect").html("");
        var form=layui.form;
        var $divMod = $('#divModify');
        var dataObj = JSON.parse($(srcDom).attr('data-entity'));
        Tool.autoInpourData2Html(dataObj, $divMod[0]);
        $divMod.find('input[name=password]').val('');
        $.ajax({
            url:'/api/auth/getRole.json',data:{operatorId:dataObj.rid}, success:function(data){
                if(data.code==100){
                    var roles=data.data.roles;
                    var roleId=data.data.roleId;
                    var html='';
                    for(var i=0;i<roles.length;i++){
                        if(roles[i].rid==roleId){
                            html+="<option value='"+roles[i].rid+"' selected>"+roles[i].roleName+"</option>";
                        }else {
                            html+="<option value='"+roles[i].rid+"'>"+roles[i].roleName+"</option>";
                        }
                    }
                    $("#uRoleSelect").append(html);
                    form.render('select')
                }else {
                    layer.alert("角色获取失败!")
                }
                layer.open({
                    type: 1,
                    title: "编辑管理员",
                    closeBtn: 1,
                    area: 'auto',
                    maxWidth: 550,
                    shadeClose: false,
                    content: $divMod
                });
            }
        });


	},


    thawing:function(srcDom) {
        layer.alert('<span style="font-size:18px;">确定冻结用户？</span>', {icon:6, area:['360px','180px'], btn:['确定']},
            function(){
                var id = $(srcDom).attr('data-id');
                var data = {"rid": id, "status": 2}
                API.thawing(JSON.stringify(data), function (resp) {
                    if (resp.code == 100) {
                        UI.tipSucc('冻结成功！', function () {
                            UI.query();
                        });
                    } else {
                        UI.tipErr(resp.msg);
                    }
                });
            });


    },

    freeze:function(srcDom) {

        layer.alert('<span style="font-size:18px;">确定解冻用户？</span>', {icon:6, area:['360px','180px'], btn:['确定']},
            function(){
                var id = $(srcDom).attr('data-id');
                var data = {"rid": id, "status": 1}
                API.thawing(JSON.stringify(data), function (resp) {
                    if (resp.code == 100) {
                        UI.tipSucc('解冻成功！', function () {
                            UI.query();
                        });
                    } else {
                        UI.tipErr(resp.msg);
                    }
                });
            });



    },

    del:function(srcDom) {
        layer.alert('<span style="font-size:18px;">确定删除用户？</span>', {icon:6, area:['360px','180px'], btn:['确定']},
            function(){
                var id = $(srcDom).attr('data-id');
                var data = {"rid": id};
                API.del(JSON.stringify(data), function (resp) {
                    if (resp.code == 100) {
                        UI.tipSucc('删除成功！', function () {
                            UI.query();
                        });
                    } else {
                        UI.tipErr(resp.msg);
                    }
                });
         });


    },

    leaveOffice:function(srcDom) {
        layer.alert('<span style="font-size:18px;">确定用户已离职？</span>', {icon:6, area:['360px','180px'], btn:['确定']},
            function(){
                var id = $(srcDom).attr('data-id');
                var data = {"rid": id,"status":3}
                API.thawing(JSON.stringify(data), function (resp) {
                    if (resp.code == 100) {
                        UI.tipSucc('离职成功！', function () {
                            UI.query();
                        });
                    } else {
                        UI.tipErr(resp.msg);
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
    thawing : function(data, callback){
		$.post('/api/operator/modStatus.json', data, function(jsonResp){
			if(callback)callback(jsonResp);
		});
	},
    freeze : function(data, callback){
        $.post('/api/operator/modStatus.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
    del : function(data, callback){
        $.post('/api/operator/del.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
    leaveOffice : function(data, callback){
        $.post('/api/operator/modStatus.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
    edit : function(data, callback){
        $.post('/api/operator/edit.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },

    modPwdByMgr : function(data, callback){
		$.post('/api/operator/modPwdByMgr.json', data, function(jsonResp){
			if(callback)callback(jsonResp);
		});
	}
}
