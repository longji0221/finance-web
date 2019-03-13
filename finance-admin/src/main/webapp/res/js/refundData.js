/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl', 'upload'], function(exports){
    UI.init();
    exports('refundData', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
    init : function(){
        /* START ---- 列表初始化 */
        layui.table.render({
            elem: '#refundDataTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/refund/listRefundData.json',
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
                {width:120, field:'userName', align:'userName', unresize: true, title: "姓名"},
                {width:150, field:'mobile', align:'mobile', unresize: true, title: "手机号"},
                {width:150, field:'type', align:'type',templet:'#typeDiv',unresize: true, title: "退款方式"},
                {width:130, field:'userAccount', align:'userAccount', unresize: true, title: "退款账户"},
                {width:120, field:'amount', templet:'amount', align:'center', unresize: true, title: "退款金额"},
                {width:180, field:'tradeNo', templet:'tradeNo', align:'center', unresize: true, title: "退款流水号"},
                {width:150, field:'operater', align:'operater', unresize: true, title: "申请人"},
                {width:200, field:'gmtCreate', align:'gmtCreate', unresize: true, title: "申请时间"},
                {width:200, field:'refundTime', align:'refundTime', unresize: true, title: "审核时间"},
                {width:150, field:'finance', align:'finance', unresize: true, title: "审核人"},
                {width:140, field:'status', align:'status',templet:'#statusDiv', unresize: true, title: "退款状态"},
            ]]
        });
        /* END ---- 列表初始化 */

        /* START ---- 日期组件初始化 */
        layui.laydate.render({
            elem: '#dateStart'
            ,type: 'datetime'
            ,theme: '#01AAED'
        });
        layui.laydate.render({
            elem: '#dateEnd'
            ,type: 'datetime'
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
        layui.table.reload('refundDataTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                mobile:$('#mobile').val(),
                status:$('#status').val()
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