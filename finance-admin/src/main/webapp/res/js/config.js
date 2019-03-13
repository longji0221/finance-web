var config = function(){
	// 页面配置jqXhr基础配置,弹框使用父容器layer
	var loadingPopup;
	$.ajaxSetup({
		cache: false,
		timeout: 12000,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		beforeSend: function(){
			loadingPopup = window.parent.layer.load(2, {time: 13*1000});
		},
		complete: function(){
			window.parent.layer.close(loadingPopup);
		},
		error: function(xhr, status, e){
			if(0 == xhr.status){ // 超时
				window.parent.layer.alert('您的网络可能断线了', {icon:0, time: 3000, closeBtn: 0});
			}else if(400 == xhr.status){ //参数请求错误
				window.parent.layer.alert('请求参数有误，请检查您的输入项', {icon:0, time: 3000, closeBtn: 0});
			}else if(401 == xhr.status){ //未验证
				window.parent.layer.alert('<span style="font-size:18px;">会话超时，需要重新登录!</span>', {icon:6, area:['360px','180px'], btn:['确定']},
							function(){
								window.parent.layer.closeAll();
								window.parent.location.href= '/login.html';
							});
			}else if(404 == xhr.status){ //请求不存在
				window.parent.layer.alert('资源不可用，请稍后重试', {icon:0, time: 3000, closeBtn: 0});
			}else if(412 == xhr.status){ //服务端主动报错
				var respObj = JSON.parse(xhr.responseText);
				window.parent.layer.alert(respObj.msg, {icon:0, time: 3000, closeBtn: 0});
			}else {
				console.log(xhr);
				console.log(status);
				console.log(e);
				window.parent.layer.alert('系统君开小差了，攻城狮正在加紧修复中，请稍后重试或联系技术！', {icon:4, area:['400px']});
			}
		}
	});
};
config();