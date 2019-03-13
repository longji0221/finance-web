/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
    UI.init();

    exports('promiseIncome', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
    init : function(){
        /*区域权限设置*/
        $.ajax({
            url:"/api/auth/divAuth.json",
            data:{menuUrl:"/pages/promiseIncome.html"},
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
            elem: '#promiseIncomeTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/promiseIncome/list.json',
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
                {width:100, field:'rid', align:'center', unresize: true, title: "ID"},
                {width:120, field:'liquidationCompany', templet:'#dlliquidationCompany', align:'center', unresize: true, title: "清算公司"},
                {width:180, field:'productName', align:'center', unresize: true, title: "产品名称"},
                {width:180, field:'borrowNo', align:'center', unresize: true, title: "借款编号"},
                {width:100, field:'periods', align:'center', unresize: true, title: "期数"},
                {width:180, field:'gmtPlanRepay',templet:'#dlGmtPlanRepay', align:'center', unresize: true, title: "应还时间"},
                {width:100, field:'predictAmount', align:'center', unresize: true, title: "应收总额"},
                {width:150, field:'repayAmount', align:'center', unresize: true, title: "累计已还金额"},
                {width:150, field:'noneAmount', align:'center', unresize: true, title: "剩余应还金额"},
                {width:150, field:'amount', align:'center', unresize: true, title: "应收借款本金"},
                {width:100, field:'retaAmount', align:'center', unresize: true, title: "应收利息"},
                {width:120, field:'poundageAmount', align:'center', unresize: true, title: "应收手续费"},
                {width:150, field:'shopAmount', align:'center', unresize: true, title: "应收搭售商品费用"},
                {width:120, field:'overdueAmount', align:'center', unresize: true, title: "应收逾期费"},
                {width:150, field:'status', align:'center', unresize: true, title: "状态"}
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
        layui.table.reload('promiseIncomeTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                borrowNo: $('#borrowNo').val(),
                liquidationCompany: $('#liquidationCompany').val(),
                gmtStart: $('#gmtStart').val(),
                gmtEnd: $('#gmtEnd').val()
            }
        });
    },


    getLiquidationCompany : function(code){
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
    Export:function() {
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
        window.open("/api/promiseIncome/export.json?" + url);
    }
}
