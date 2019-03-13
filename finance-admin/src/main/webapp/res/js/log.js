/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
    UI.init();

    exports('log', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
    init : function(){
        /* START ---- 列表初始化 */
        layui.table.render({
            elem: '#logTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/log/list',
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
                {width:110, field:'rid',templet:'dlCompany', align:'center', unresize: true, title: "序号"},
                {width:120, field:'userId', align:'center', unresize: true, title: "用户ID"},
                {width:210, field:'username', align:'center', unresize: true, title: "用户手机号"},
                {width:220, field:'operation', align:'center', unresize: true, title: "操作"},
                {width:110, field:'time', align:'center', unresize: true, title: "用时(毫秒)"},
                {width:320, field:'method', align:'center', unresize: true, title: "方法"},
                {width:220, field:'params', align:'center', unresize: true, title: "参数"},
                {width:150, field:'ip', align:'center', unresize: true, title: "IP地址"},
                {width:180, field:'gmtCreate', align:'center', unresize: true, title: "创建时间"}
            ]]
        });
        /* END ---- 列表初始化 */


        /* START ---- 日期组件初始化 */
        layui.laydate.render({
            elem: '#gmtStart'
            ,max: 1
            ,theme: '#01AAED'
        });
        layui.laydate.render({
            elem: '#gmtEnd'
            ,max: 1
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
        layui.table.reload('logTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                userName: $('#userName').val(),
                operation: $('#operation').val()
            }
        });
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

}
