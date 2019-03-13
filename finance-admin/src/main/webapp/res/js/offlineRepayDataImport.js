/**
 宝贝违禁词功能JS
 **/
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl', 'upload'], function(exports){
    UI.init();

    exports('offlineRepayDataImport', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
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
                    html+="<option value='' selected=''>收款账户</option>";
                    for(var i=0;i<benefitAccount.length;i++){
                        html+="<option value='"+benefitAccount[i].name+"'>"+benefitAccount[i].name+"</option>";
                    }
                    html+="<option value='未知账户'>未知账户</option>"
                    $("#benefitAccount").append(html);
                    form.render('select')
                }else {
                    layer.alert("收款账户获取失败!")
                }
            }
        });
        /*START  ----文件上传*/
        var upload=layui.upload;
        upload.render({
            elem:'#test1'
            ,url:'/api/offlineRepaymentDataImport/uploadExcel'
            ,accept:'file'
            ,exts: 'xlsx|xls'
            ,multiple:true
            ,auto:false
            ,bindAction: '#sbtnUpload'
            ,done:function (res) {
                //上传完毕回调
                if(res.code==100){
                    layer.msg('账单文件上传成功');
                    UI.uploadQuery();
                }else {
                    layer.msg(res.msg==null?'请求失败':res.msg);
                }
            }
        });
        /* END   ----文件上传*/

        /* START ---- 列表初始化 */
        layui.table.render({
            elem: '#importDataTable',
            cellMinWidth: 80,
            height: 470,
            page: true,
            loading: false,
            url: '/api/offlineRepaymentDataImport/listOfflineRepayData.json',
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
                {width:260, field:'payTime', align:'payTime', unresize: true, title: "入帐时间"},
                {width:300, field:'tradeNo', align:'tradeNo', unresize: true, title: "支付宝交易号"},
                {width:200, field:'amount', align:'amount', unresize: true, title: "金额"},
                {width:240, field:'payerAccount', align:'payerAccount', unresize: true, title: "交易账户"},
                {width:200, field:'realName', templet:'realName', align:'center', unresize: true, title: "姓名"},
                {width:220, field:'benefitAccount', templet:'benefitAccount', align:'center', unresize: true, title: "收款账户"},
                {width:270, field:'remark', align:'remark', unresize: true, title: "备注"},
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
        if($('#dateEnd').val() == "" &&$('#dateStart').val() != ""){
            return layer.alert('结束日期不能为空');
        }
        if($('#dateEnd').val() != "" &&$('#dateStart').val() == ""){
            return layer.alert('起始日期不能为空');
        }
        if($('#dateEnd').val()<$('#dateStart').val()){
            return layer.alert('结束日期不能小于开始日期');
        }
        layui.table.reload('importDataTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                payerAccount: $('#payerAccount').val(),
                tradeNo: $('#tradeNo').val(),
                dateStart: $('#dateStart').val(),
                dateEnd: $('#dateEnd').val(),
                benefitAccount:$('#benefitAccount').val()
            }
        });
    },
    /**
     * 文件上传查询函数
     */
    uploadQuery: function(){
        layui.table.reload('importDataTable', {
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                payerAccount: $('#payerAccount').val(),
                tradeNo: $('#tradeNo').val(),
                dateStart: $('#dateStart').val(),
                dateEnd: $('#dateEnd').val(),
                benefitAccount:$('#benefitAccount').val()
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