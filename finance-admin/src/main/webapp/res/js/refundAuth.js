/**
  宝贝违禁词功能JS
**/ 
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
	UI.init();
	
	exports('refundAuth', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
	init : function(){
		/* START ---- 列表初始化 */
		layui.table.render({
		  elem: '#refundTable',
		  cellMinWidth: 80,
		  height: 470,
		  page: true,
		  loading: false,
		  url: '/api/refund/listRefund.json',
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
          where: {
                status: $('#status').val(),
          },
		  response: { statusCode: 100 },
	      cols: [[ //表头
              {width:100, field:'rid', align:'rid', unresize: true, title: "ID"},
              {width:100, field:'userName', align:'userName', unresize: true, title: "姓名"},
              {width:150, field:'mobile', align:'mobile', unresize: true, title: "手机号"},
              {width:150, field:'idNumber', align:'idNumber', unresize: true, title: "身份证"},
              {width:100, field:'operater', align:'operater', unresize: true, title: "申请人"},
              {width:220, field:'gmtCreate', align:'gmtCreate', unresize: true, title: "申请时间"},
              {width:100, field:'status',templet:'#statusDiv', align:'status', unresize: true, title: "状态"},
              {width:120, field:'amount', templet:'amount', align:'center', unresize: true, title: "退款金额"},
              {width:140, field:'userAccount', align:'userAccount', unresize: true, title: "退款账户"},
              {width:140, field:'type', templet:'#typeDiv',align:'type', unresize: true, title: "退款方式"},
              {width:220, field:'remark', align:'remark', unresize: true, title: "审核说明"},
              {width:150, templet:'#reviewDiv', align:'center', unresize: true, title: "操作",event	:'none', style:'cursor: pointer;'}
          ]]
		});
		/* END ---- 列表初始化 */


		/* START ---- 日期组件初始化 */
		layui.laydate.render({
		  elem: '#dateStart'
		  ,max: 1
		  ,theme: '#01AAED'
		});
		layui.laydate.render({
		  elem: '#dateEnd'
		  ,max: 1
		  ,theme: '#01AAED'
		});


	    /* START --- 事件监听区域  ----- */
		$('#btnSearch').click(function(){
			UI.query();
			return false;
		});
		
		$('#btnClear').click(function(){
			$('#formQuery')[0].reset();
			return false;
		});
		
		$('#btnShowCreate').click(function(){
			UI.showCreatePopup();
			return false;
		});
		
		$('#btnExport').click(function(){
			// TODO
			return false;
		});
		/* END --- 事件监听区域  ----- */
	},


    review:function(datasrc){
          var data =  JSON.parse($(datasrc).attr('data-entity'));
         //获得当前行数据
            var type=data.type;
            if(type=='ISJREBATE'){
                $("#iFefundAmount").text(data.amount);
                layer.open({
                    type: 1,
                    title: "审核",
                    area : [ "300px","250px" ],
                    btnAlign: 'c',
                    btn:["拒绝","通过"],
                    zIndex: layer.zIndex,
                    content:$("#isjRefund"),
                    btn1:function(index){
                        $.ajax({
                            url:"/api/refund/reviewRefuse.json",
                            data:{amount:data.amount,mobile:data.mobile,id:data.rid},
                            success:function (data) {
                                if(data.code==100){
                                    layer.msg("审核成功")

                                }else {
                                    layer.msg("审核异常")
                                }
                                layui.table.reload('refundTable', {
                                    page: {
                                        curr: 1 //重新从第 1 页开始
                                    },
                                });
                                layer.close(index);

                            }

                        })

                    },
                    btn2:function(index1){
                        $.ajax({
                            url:"/api/refund/reviewPass.json",
                            data:{amount:data.amount,mobile:data.mobile,id:data.rid,type:data.type,remark:data.remark},
                            success:function (data) {
                                if(data.code==100){
                                    layer.msg("审核成功")
                                }else {
                                    layer.msg("审核异常")
                                }
                                layer.close(index1);

                                layui.table.reload('refundTable', {
                                    page: {
                                        curr: 1 //重新从第 1 页开始
                                    },
                                });

                            }

                        })
                    }
                })
            }else {
                $("#oRefundAmount").text(data.amount);
                $("#oFefundAccount").text(data.userAccount);
                layer.open({
                    type: 1,
                    title: "审核",
                    area : [ "300px","250px" ],
                    btnAlign: 'c',
                    btn:["拒绝","通过"],
                    shadeClose: false,
                    zIndex : layer.zIndex,
                    content:$("#offlineRefund"),
                    btn1:function(index){
                        $.ajax({
                            url:"/api/refund/reviewRefuse.json",
                            data:{amount:data.amount,mobile:data.mobile,id:data.rid},
                            success:function (data) {
                                if(data.code==100){
                                    layer.msg("审核成功")

                                }else {
                                    layer.msg("审核异常")
                                }
                                layer.close(index);
                                layui.table.reload('refundTable', {
                                    page: {
                                        curr: 1 //重新从第 1 页开始
                                    },
                                });
                            }

                        })
                    },
                    btn2:function(index1){
                        $.ajax({
                            url:"/api/refund/reviewPass.json",
                            data:{amount:data.amount,mobile:data.mobile,id:data.rid,type:data.type,remark:data.remark},
                            success:function (data) {
                                if(data.code==100){
                                    layer.msg("审核成功")
                                }else {
                                    layer.msg("审核异常")
                                }
                                layer.close(index1);
                                layui.table.reload('refundTable', {
                                    page: {
                                        curr: 1 //重新从第 1 页开始
                                    },
                                });
                            }

                        })
                    }
                })
            }



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
        var status=$('#status').val();
        layui.table.reload('refundTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                mobile: $('#mobile').val(),
                status: status,
                type: $('#type').val(),

            }
        });


	},
	
	/**
	 * 弹框-修改管理员
	 */
	showModifyPopup: function(srcDom){
		var $divMod = $('#divModify');
		var dataObj = JSON.parse($(srcDom).attr('data-entity'));
		Tool.autoInpourData2Html(dataObj, $divMod[0]);
		$divMod.find('input[name=password]').val('');
		layer.open({
		  type: 1,
		  title: "编辑管理员",
		  closeBtn: 1,
		  area: 'auto',
		  maxWidth: 550,
		  shadeClose: false,
		  content: $divMod
		});
	},
	
	/**
	 * 弹框-新增管理员
	 */
	showCreatePopup: function(){
		layer.open({
		  type: 1,
		  title: "新增管理员",
		  closeBtn: 1,
		  area: 'auto',
		  maxWidth: 550,
		  shadeClose: false,
		  content: $('#divCreate')
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
	edit : function(data, callback){
		$.post('/api/operator/edit.json', data, function(jsonResp){
			if(callback)callback(jsonResp);
		});
	}
}
