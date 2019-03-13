layui.define(['layer', 'form', 'element'], function(exports){
    // 页面layer基础配置
    layer.config({
        offset: '240px'
    });
    
    UI.init();
    
    exports('login', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
	init : function(){
		var form = layui.form;
	    form.on('submit(sbtnLogin)', function(laydata){
	    	console.log(laydata);
	    	var reqData = laydata.field;
	    	API.login(JSON.stringify(reqData), function(resp){
	    		if(resp.code == 100){
    			  UI.tipSucc('登录成功！', function(){
    				  location.href= '/main.html';
    			  });
	    		}else{
    			  UI.tipErr(resp.msg);
	    		}
	    	});
	    	
	    	return false;
	    });
	},

	tipSucc : function(msg, callback){
		layer.closeAll();
		layer.msg(msg, {
			icon: 1,
			time: 2000 //2秒关闭
		}, callback);
	},
	
	tipErr : function(msg){
		layer.msg(msg, {icon: 5, offset: '70px', time:3000});
	}
};

/**
 * ------------- 与服务端通信模块 --------------
 */
var API = {

	login : function(data, callback){
		$.post('/api/login/smsIn.json', data, function(jsonResp){
			if(callback)callback(jsonResp);
		});
	},
    sendVerify : function(){
        var data={"username":$('#username').val()};
        $.ajax({
            type: "post",
            contentType:"application/json",
            url: "/api/login/sendSms.json",
            data: JSON.stringify(data),
            success: function(data){
                if(data.code==100){
                    layer.open({
                        title: '提示'
                        ,content: '验证码发送成功'
                    });
                }else {
                    layer.open({
                        title: '提示'
                        ,content: data.msg
                    });
                }
            }
        })
    }


}