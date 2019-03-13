layui.define(['layer', 'form', 'element'], function(exports){
	// 页面layer基础配置
	layer.config({
		offset: '240px'
	});
	
	UI.init();
	
  	exports('main', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
	init:function(){
		this.listen();
		this.processLoginStatus();

        /* START ---- 数据上送初始化 */
        var form = layui.form;
        form.on('submit(sbtnModify)', function(laydata){
            var reqData = laydata.field;
            API.modPwd(JSON.stringify(reqData), function(resp){
                if(resp.code == 100){
                    UI.tipSucc('修改成功！', function(){
                    	$("#oriPasswd").val("");
                        $("#newPasswd").val("");
                        window.location.href="/login.html"
                    });
                }else{
                    UI.tipErr(resp.msg);
                }
            });

            return false;
        });
        /* END ---- 数据上送初始化 */
	},
	
	processLoginStatus: function(){
		var tipWelcome = function(){
			layer.msg('欢迎回来', {
				icon: 6,
				time: 2000 //2秒关闭
			}, function(){
				layer.closeAll();
			});
		}
		
		API.getLoginStatus(function(jsonResp){
			var isLogin = jsonResp.data.isLogin;
			var username = jsonResp.data.username;
			var menuIds = jsonResp.data.menuIds;
			if(isLogin == true){
				UI.renderLoginInfo(username);
				var menuIdArrs = menuIds.split(',');
				for(var i = 0; i < menuIdArrs.length; i++ ){
					$('#divNavLeft a[data-id='+ menuIdArrs[i] +']').parent().show();
				}
			}else{
				layer.alert('<span style="font-size:18px;">会话超时，需要重新登录!</span>', {icon:6, area:['360px','180px'], btn:['确定']},
					function(){
						layer.closeAll();
						location.href= '/login.html';
					});
			}
		});
	},
	/**
	 * iframe高度自适应
	 */
	adapt: function(tarIframe){
		tarIframe.height=tarIframe.contentDocument.querySelector("body").scrollHeight;
	},
	
	/**
	 * 页面全局时间监听器
	 */
	listen : function() {
		$('#divNavLeft a[data-click-url]').click(function(event) {
			var $nav = $(this);
			var title = $nav.text();
			var url = $nav.attr("data-click-url") + "?" + new Date().getTime();
			var navId = $nav.attr("data-id");
			
			if($('#divTabBody').find('li[lay-id=' + navId + ']').length > 0){
				layui.element.tabChange('divTabBody', navId);
				/*$('#iframe'+navId)[0].src = url; 刷新页面*/
				return;
			}
			
			layui.element.tabAdd('divTabBody', {
				title : title,
				content : '<iframe class="iframe-template" onload="UI.adapt(this);" src="' + url + '" id="iframe' + navId + '"></iframe>',
				id : navId
			});
			
			layui.element.tabChange('divTabBody', navId);
		});
	},
	
	renderLoginInfo: function(username){
		$('#username').text(username);
	},
	
	/**
	 * 登出
	 */
	logout : function(){
		API.logout(function(jsonResp){
			if(jsonResp.code == 100 ){
				layer.msg("退出成功")
				window.location.href="login.html";
			}else{
				UI.tipErr(jsonResp.msg);
			}
		});
	},

    /**
     * 弹框-修改密码
     */
    showChangePwdPopup: function(srcDom){
        var $divMod = $('#divModify');
        $divMod.find('input[name=password]').val('');
        layer.open({
            type: 1,
            title: "修改密码",
            closeBtn: 1,
            area: 'auto',
            maxWidth: 550,
            shadeClose: false,
            content: $divMod
        });
    },
    /**
     * 弹框 - 失败提示
     */
	tipErr : function(msg){
		layer.msg(msg, {icon: 5, offset: '70px',time:4000});
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
};

/**
 * ------------- 与服务端通信模块--------------
 */
var API = {
	getLoginStatus: function(callback){
		$.post("/api/login/status.json", function(jsonResp){
			if(callback)callback(jsonResp);
		});
	},
	logout: function(callback){
		$.post("/api/login/out.json", function(jsonResp){
			if(callback)callback(jsonResp);
		});
	},
    modPwd : function(data, callback){
        $.post('/api/operator/modPwd.json', data, function(jsonResp){
            if(callback)callback(jsonResp);
        });
    }
}