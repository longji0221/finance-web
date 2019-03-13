layui.define(['layer'], function(exports){
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

});
function pre(){
	try{
		// check 是否在iframe中打开，且容器是否合法，如果没有容器parent属性总是返回自身window对象
		var parentWid = window.parent;
		if(location.href == parentWid.location.href){
			parentWid.location.replace(location.protocol+"//"+location.host+"/main.html?childUrl="+ location.pathname);
			return;
		}


        if(location.host != parentWid.location.host){
			location.replace("/err.html");
		}
	}catch(e){
		location.replace("/err.html");
	}
}
var API = {
    getLoginStatus: function(callback){
        $.post("/api/login/status.json", function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
}
pre();