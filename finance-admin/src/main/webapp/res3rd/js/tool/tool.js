var Tool = {
	getRequest : function(){
	   var url = location.search; //获取url中"?"符后的字串
	   var theRequest = new Object();
	   if (url.indexOf("?") != -1) {
	      var strs = url.substr(1).split("&");
	      for(var i = 0; i < strs.length; i ++) {
	         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
	      }
	   }
	   return theRequest;
	},
	
	trim : function(source,isGlobal){
		 var result;
         result = source.replace(/(^\s*)|(\s*$)/g,"");
         if(isGlobal){
             result = result.replace(/\s/g,"");
         }
         return result;
	},
	
	//工具函数
	genUrlByParams :function(url,queryParams){
		var tmpUrl = url;
		var firstFlag = 0;
		for(var name in queryParams){
			if(firstFlag != 0){
				tmpUrl += "&"+name+"="+queryParams[name];
			}else{
				tmpUrl += "?"+name+"="+queryParams[name];
			}
			firstFlag++;
		}
		return tmpUrl;
	},
	
	/**
	 * paramsObj:键-值集合{"key":"value",...}
	 * domParent:父原生dom对象，其子类交互元素必须包含name属性，否则无法完成自动赋值
	 * 
	 * 键名称对应domParent下name值,目前只支持input、select和一般标签的textContent注入
	 * 
	 * 注意：此方法只支持html5页面
	 */
	autoInpourData2Html : function(paramsObj,domParent,options){
		//{} options
		
		if(!domParent || !paramsObj){
			return;
		}
		
		//重置dom内输入元素的内容
		if(domParent.tagName == "FORM"){
			domParent.reset();
		} else {
			var eles = domParent.querySelectorAll("[name]");
			for(var i=0;i<eles.length;i++){
				if(eles[i].tagName == "INPUT" || eles[i].tagName == "TEXTAREA" || eles[i].tagName == "SELECT"){
					if(eles[i].type == 'color'){ //颜色标签特殊处理
					}else{
					  eles[i].value = "";
					}
				}
			}
		}
		
		//注入
		for(var name in paramsObj){
			var ele = domParent.querySelector("[name="+name+"]");
			if(ele){
				if(ele.tagName == 'SELECT'){
					var opts = ele.getElementsByTagName("OPTION");
					for(var i=0;i<opts.length;i++){
						if(String(paramsObj[name]) == opts[i].value){
							opts[i].selected = true;
							break;
						}
					}
				}else if(ele.tagName == 'INPUT' || ele.tagName == 'TEXTAREA'){
					ele.value = paramsObj[name];
				}else{
					ele.textContent = paramsObj[name];
				}
			}
		}
	},
	/**
	 * 将srcObj的属性值传递到distObj中，仅对distObj已经重名的属性名赋值
	 */
	transfer : function(srcObj, distObj){
	  for(var name in distObj){
		  distObj[name] = srcObj[name];	    
	  }
	},
	serializeObj : function(domParent){
		if(!domParent){
			return null;
		}
		
		var obj = {};
		var eles = domParent.querySelectorAll("[name]");
		for(var i=0;i<eles.length;i++){
			if(eles[i].tagName == "INPUT" || eles[i].tagName == "TEXTAREA" || eles[i].tagName == "SELECT"){
				obj[eles[i].name] = eles[i].value;
			}
		}
		
		return obj;
	},
	
	RegexUtil : {
		/**
		 * 检查目标字符串是否是数字
		 * @param source
		 * @param length 数字长度最大值,可选参数
		 */
		isNum : function(source,length){
			var regExp = new RegExp("^\\d"+(length?"{1,"+length+"}":"")+"$");
			return regExp.test(String(source));
		},
		
		/**
		 * 检查目标字符串是否包含指定长度以上的连续数字
		 * @param source
		 * @param length 连续数字的长度
		 */
		hasNum : function(source,length){
			var regExp = new RegExp("\\d"+(length?"{"+length+"}":""));
			return regExp.test(String(source));
		},

		/**
		 * 检查是否是合法链接
		 * @param source
		 * @param length 数字长度最大值,可选参数
		 */
		isHttp : function(source){
			var regExp = new RegExp("^(http|ftp|https)://[a-zA-Z0-9]+\.[a-zA-Z0-9]+\.[a-zA-Z0-9]+/.+$");
			return regExp.test(String(source));
		}
	},
	
	StringFormat : {
		leftPad : function(tarStr, length, replace){
			if(tarStr.length <length){
				var dev = length-tarStr.length;
				for(var i = 0;i<dev;i++){
					tarStr = replace+tarStr;
				}
			}
			return tarStr;
		},
		cutLeft : function(tarStr,length){
			tarStr.slice(2);
		}
	},
	
	EventUtil : {
		/**
		 * 获取事件对象 
		 */
		getEvent : function(event) {
			return event || window.event;
		},
		/**
		 * 获取事件主人 
		 */
		getTarget : function(event) {
			return event.target || event.srcElement;
		},
		preventDefault : function() {
			if (event.preventDefault) {
				event.preventDefault();
			} else {
				event.returnValue = false;
			}
		},
		/**
		 * 阻止事件冒泡 
		 */
		stopPropagation : function() {
			if (event.stopPropagation) {
				event.stopPropagation();
			} else {
				event.cancelBubble = true;
			}
		},
		addHandler : function(element, type, handler) {
			if (element.addEventListener) {
				element.addEventListener(type, handler, false);
			} else if (element.attachEvent) {
				element["e" + type] = function() {
					handler.call(element)
				}
				element.attachEvent("on" + type, element["e" + type]);
			} else {
				element["on" + type] = handler;
			}
		},
		removeHandler : function(element, type, handler) {
			if (element.removeEventListener) {
				element.removeEventListener(type, handler, false);
			} else if (element.detachEvent) {
				element.detachEvent("on" + type, element["e" + type]);
				element["e" + type] = null;
			} else {
				element["on" + type] = null;
			}
		}
	}
	
}