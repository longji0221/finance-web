/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
    UI.init();

    exports('actualPaid', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
    init : function(){
        /*区域权限设置*/
        $.ajax({
            url:"/api/auth/divAuth.json",
            data:{menuUrl:"/pages/actualPaid.html"},
            success:function(result) {
                if(result.data!=null){
                    var menuIdArrs = result.data.split(',');
                    for(var i = 0; i < menuIdArrs.length; i++ ){
                        $('#'+ menuIdArrs[i]+'Where').show();
                        $('.'+ menuIdArrs[i]+'Where').show();
                        $('#'+ menuIdArrs[i]+'').show();
                    }
                }
            },
            error:function () {
                layer.msg("请求异常")
            }
        })
        /* START ---- 列表初始化 */
        layui.table.render({
            elem: '#actualPaidTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/actualPaid/list.json',
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
                {width:200, field:'liquidationCompany',templet:'dlCompany', align:'center', unresize: true, title: "清算公司"},
                {width:210, field:'productType', align:'center', templet:'#dlProductType', unresize: true, title: "产品类型"},
                {width:210, field:'productName', align:'center', unresize: true, title: "产品名称"},
                {width:220, field:'borrowNo', align:'center', unresize: true, title: "借款编号"},
                {width:210, field:'actualAmount', align:'center', unresize: true, title: "实际金额"},
                {width:220, field:'actualTime', templet:'#dlGmtCreate', align:'center', unresize: true, title: "实际付款时间"},
                {width:210, field:'tppNid', align:'center', unresize: true, title: "支付渠道"},
                {width:210, field:'payTradeNo', align:'center', unresize: true, title: "支付流水号"}
            ]]
        });
        /* END ---- 列表初始化 */


        /* START ---- 日期组件初始化 */
        layui.laydate.render({
            elem: '#gmtStart'
            ,theme: '#01AAED'
        });
        layui.laydate.render({
            elem: '#gmtEnd'
            ,theme: '#01AAED'
        });
        /* END ---- 日期组件初始化 */

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
        layui.table.reload('actualPaidTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                borrowNo: $('#borrowNo').val(),
                productType: $('#productType').val(),
                liquidationCompany: $('#liquidationCompany').val(),
                gmtStart: $('#gmtStart').val(),
                gmtEnd: $('#gmtEnd').val()
            }
        });
    },

    getProductType : function(code){
        if(code == 'CASHINSTALMENT'){
            return '现金分期';
        }else if(code == 'BORROWCASH'){
            return '信用贷';
        }else if(code == 'BORROW'){
            return '消费分期';
        } else {
            return '其他';
        }
    },

    getCompany : function(code){
        if(code == 'ISHANGJIE'){
            return '爱上街';
        }else if(code == 'DSED'){
            return '绿游';
        }else if(code == 'JSD'){
            return '绿游';
        } else {
            return '其他';
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
    Export : function() {

        var gmtStart = $('#gmtStart').val();
        var gmtEnd = $('#gmtEnd').val();

        if (!gmtStart) {
            alert("导出开始时间不能为空！");
            return;
        }
        if (!gmtEnd) {
            alert("导出结束时间不能为空！");
            return;
        }
        var url =  "gmtStart="+$('#gmtStart').val()+"&gmtEnd="+$('#gmtEnd').val()+ "&borrowNo=" + $('#borrowNo').val() + "&liquidationCompany=" + $('#liquidationCompany').val()+ "&productType=" + $('#productType').val();
        window.open("/api/actualPaid/export.json?" + url);
    }

}
