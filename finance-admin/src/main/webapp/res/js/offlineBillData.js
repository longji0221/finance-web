/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl', 'upload'], function(exports){
    UI.init();

    exports('offlineBillData', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});


/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
    init : function(){
        var form=layui.form;
        $.ajax({
            url:'/api/offlineRepaymentDataImport/getBenefitAccount.json', success:function(data){
                if(data.code==100){
                    $("#benefitAccount").html("")
                    var benefitAccount=data.data;
                    var html='';
                    html+="<option value='' selected=''>全部</option>";
                    for(var i=0;i<benefitAccount.length;i++){
                        html+="<option value='"+benefitAccount[i].name+"'>"+benefitAccount[i].name+"</option>";
                    }
                    html+="<option value='未知账户'>未知账户</option>"
                    $("#benefitAccount").append(html);
                    form.render('select')
                }else {
                    layer.alert("支付宝账户获取失败!")
                }
            }
        });
        /*区域权限设置*/
        $.ajax({
            url:"/api/auth/divAuth.json",
            data:{menuUrl:"/pages/offlineBillData.html"},
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
            elem: '#billTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/offlineBIllData/listOfflineBillData.json',
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
                {width:423, field:'time',align:'payTime', unresize: true, title: "日期"},
                {width:422, field:'sumAmount', align:'sumAmount', unresize: true, title: "当日总入账（当日被认领）"},
                {width:422, field:'alipayAmount', align:'alipayAmount', unresize: true, title: "支付宝当日入账（当日被认领）"},
                {width:423, field:'bankAmount', align:'bankAmount', unresize: true, title: "银行卡"},
            ]]
        });
        /* END ---- 列表初始化 */


        /* START ---- 日期组件初始化 */
        layui.laydate.render({
            elem: '#dateStart'
            ,theme: '#01AAED'
        });
        layui.laydate.render({
            elem: '#dateEnd'
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
        if($('#dateEnd').val() == "" ||$('#dateStart').val() == ""){
            return layer.alert('查询的日期不能为空');
        }
        if($('#dateEnd').val()<$('#dateStart').val()){
            return layer.alert('结束日期不能小于开始日期');
        }
        layui.table.reload('billTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                dateStart: $('#dateStart').val(),
                dateEnd: $('#dateEnd').val(),
                benefitAccount:$('#benefitAccount').val()
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

    Export:function() {
        var gmtStart = $('#dateStart').val();
        var gmtEnd = $('#dateEnd').val();
        if (!gmtStart) {
            alert("导出开始时间不能为空！");
            return;
        }
        if (!gmtEnd) {
            alert("导出结束时间不能为空！");
            return;
        }
        var time = new Date(gmtEnd).getTime() - new Date(gmtStart).getTime();
        var days = Math.floor(time / (24 * 3600 * 1000))
        if(days>1000){
            alert("只能导出1000内天数据！");
            return;
        }
        var url = "dateStart=" + gmtStart + "&dateEnd=" + gmtEnd;
        window.open("/api/offlineBIllData/export.json?" + url);
    }
}