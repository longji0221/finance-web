/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
    UI.init();

    exports('offlineRepayDataQuery', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
    init : function(){
        /* START ---- 列表初始化 */
        layui.table.render({
            elem: '#offlineDataTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/offlineRepaymentDataQuery/getOfflineRepaymentData.json',
            contentType: 'application/json; charset=utf-8',
            method: 'post',
            parseData: function(list){ //res 即为原始返回的数据
                return {
                    "code": list.code, //解析接口状态
                    "msg": list.msg, //解析提示文本
                    "count": list.data.totalCount, //解析数据长度
                    "data": list.data.list //解析数据列表
                };
            },
            response: { statusCode: 100 },
            cols: [[ //表头
                {width:250, field:'payTime', align:'payTime', unresize: true, title: "入帐时间"},
                {width:280, field:'tradeNo', align:'tradeNo', unresize: true, title: "支付宝交易号"},
                {width:200, field:'amount', align:'amount', unresize: true, title: "金额"},
                {width:200, field:'payerAccount', align:'payerAccount', unresize: true, title: "交易账户"},
                {width:200, field:'realName', templet:'realName', align:'center', unresize: true, title: "姓名"},
                {width:150, field:'benefitAccount', align:'benefitAccount', unresize: true, title: "收款账户"},
                {width:140, field:'isUse', align:'isUse', unresize: true, title: "是否使用"},
                {width:280, field:'remark', align:'remark', unresize: true, title: "备注"},
            ]]
        });
        /* END ---- 列表初始化 */

        /* START --- 事件监听区域  ----- */
        $('#btnSearch').click(function(){
            UI.query();
            return false;
        });

        $('#btnClear').click(function(){
            $('#formQuery')[0].reset();
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
        layui.table.reload('offlineDataTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                condition: $('#condition').val(),
                repayType: $('#repayType').val(),
                valueContent: $('#valueContent').val()
            }
        });
    },
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


}