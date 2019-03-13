/**
  宝贝违禁词功能JS
**/ 
layui.define(['layer', 'form', 'element', 'laydate', 'table', 'laytpl'], function(exports){
	UI.init();

	exports('user', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

/**
 * ---------------- 页面交互中心 --------------
 */
var UI={
	init : function(){

        /*区域权限设置*/
        $.ajax({
            url:"/api/auth/divAuth.json",
            data:{menuUrl:"/pages/user.html"},
            success:function(result) {
                var menuIdArrs = result.data.split(',');
                var html='';
                var thirdHtml='';
                var text='查看案件'
                for(var i = 0; i < menuIdArrs.length; i++ ){
                    if(menuIdArrs[i]=='amountRecord'){
                        $("#amountRecordBtn").show();
                    }else {
                        if(menuIdArrs[i]=='offlineRepay'){
                            text='线下还款'
                        }else if(menuIdArrs[i]=='inAccount') {
                            text='线下入账'
                        }else if(menuIdArrs[i]=='refund'){
                            text='退款'
                        }
                        html+="<a class='layui-btn  layui-btn-xs' lay-event='"+menuIdArrs[i]+"' >"+text+"</a>"
                        if( menuIdArrs[i]=='detail'){
                            thirdHtml+= "<a class='layui-btn layui-btn-primary layui-btn-xs' lay-event='"+menuIdArrs[i]+"' >查看案件</a>";
                        }
                    }
                }
                $("#barBorrowTable").append(html);
                $("#thirdBarBorrowTable").append(thirdHtml)
            },
            error:function () {
                layer.msg("请求异常")
            }
        })
		// 用户查询表
		layui.table.render({
		  elem: '#userTable',
		  cellMinWidth: 130,
		  height: 80,
		  page: false,
		  loading: false,
		  url: '/api/user/getUserInfo.json',
		  contentType: 'application/json; charset=utf-8',
		  method: 'post',
           parseData: function(res){ //res 即为原始返回的数据
                return {
                    "code": res.code,//解析接口状态
                    "msg": res.msg, //解析提示文本
                    "data": res.data //解析数据列表
                };
            },
            response: { statusCode: 100 },
            cols: [[ //表头
			  {width:200, field:'rid',unresize: true,  align:'center',  title: "用户ID"},
              {width:200, field:'userName',unresize: true,  align:'center',  title: "用户名"},
			  {width:200, field:'mobile',unresize: true,  align:'center',  title: "手机号"},
			  {width:200, field:'gender', unresize: true, templet:'#genderDiv',align:'center', title: "性别"},
			  {width:240, field:'birthday',unresize: true,  align:'center', title: "出生年月"},
			  {width:250, field:'idNumber', unresize: true,  align:'center', title: "身份证号"},
              {width:200, field:'gmtCreate', unresize: true,  align:'center', title: "创建时间"},
              {width:200, field:'gmtModified', unresize: true,  align:'center', title: "修改时间"}
            ]],
		});

        //借款信息表
        layui.table.render({
            elem: '#borrowTable',
            cellMinWidth: 100,
            height: 450,
            page: false,
            loading: false,
            url: '/api/user/listBorrow.json',
            contentType: 'application/json; charset=utf-8',
            method: 'post',
            done: function(res, curr, count){
                $("[data-field='overdueAmount']").css('display','none');
                $("[data-field='day']").css('display','none');
                $("[data-field='noReductionAmount']").css('display','none');
                $("[data-field='companyId']").css('display','none');
            },
            parseData: function(res){ //res 即为原始返回的数据
                return {
                    "code": res.code,//解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.data.totalCount, //解析数据长度
                    "data": res.data.list //解析数据列表
                };
            },
            response: { statusCode: 100 },
            cols: [[ //表头
                // {field: 'overdueAmount',style:'display:none;'},
                // {field: 'day',style:'display:none;'},
                // {field: 'noReductionAmount',style:'display:none;'},
                // {field: 'companyId',style:'display:none;'},
                {width:110, field:'company',unresize: true,  align:'center',  title: "清算公司"},
                {width:110, field:'productType',unresize: true,  align:'center', templet:'#produceTypeDiv', title: "产品类型"},
                {width:100, field:'productName',unresize: true,  align:'center',  title: "产品名称"},
                {width:200, field:'orderNo', unresize: true, align:'center', title: "订单号"},
                {width:200, field:'tradeTime',tradeTime: true,  align:'center', templet:'#tradeTimeDiv',title: "交易时间"},
                {width:100, field:'sumAmount', unresize: true,  align:'center', title: "应还金额"},
                {width:100, field:'repayAmount', unresize: true,  align:'center', title: "累计已还"},
                {width:110, field:'restAmount', unresize: true,  align:'center', title: "剩余应还"},
                {width:110, field:'refundAmount', unresize: true,  align:'center', title: "可退金额"},
                {width:150, field:'planRepayTime', unresize: true,  align:'center',templet:'#divlistGmt', title: "应还时间"},
                {width:100, field:'status', unresize: true,  align:'center', templet:'#statusDiv',title: "状态"},
                {fixed: 'right', width: 300, align:'center', toolbar: '#barBorrowTable',title: "操作"}
            ]],
        });


        //三方借款信息表
        layui.table.render({
            elem: '#thirdBorrowTable',
            cellMinWidth: 100,
            height: 450,
            page: false,
            loading: false,
            url: '/api/user/listBorrow.json',
            contentType: 'application/json; charset=utf-8',
            method: 'post',
            done: function(res, curr, count){
                $("[data-field='overdueAmount']").css('display','none');
                $("[data-field='day']").css('display','none');
                $("[data-field='noReductionAmount']").css('display','none');
                $("[data-field='companyId']").css('display','none');
            },
            parseData: function(res){ //res 即为原始返回的数据
                return {
                    "code": res.code,//解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.data.totalCount, //解析数据长度
                    "data": res.data.list //解析数据列表
                };
            },
            response: { statusCode: 100 },
            cols: [[ //表头
                // {field: 'overdueAmount',style:'display:none;'},
                // {field: 'day',style:'display:none;'},
                // {field: 'noReductionAmount',style:'display:none;'},
                // {field: 'companyId',style:'display:none;'},
                {width:180, field:'company',unresize: true,  align:'center',  title: "清算公司"},
                {width:150, field:'productType',unresize: true,  align:'center', templet:'#produceTypeDiv', title: "产品类型"},
                {width:100, field:'productName',unresize: true,  align:'center',  title: "产品名称"},
                {width:200, field:'orderNo', unresize: true, align:'center', title: "订单号"},
                {width:200, field:'tradeTime',tradeTime: true,  align:'center', templet:'#tradeTimeDiv',title: "交易时间"},
                {width:100, field:'sumAmount', unresize: true,  align:'center', title: "应还金额"},
                {width:100, field:'repayAmount', unresize: true,  align:'center', title: "累计已还"},
                {width:110, field:'restAmount', unresize: true,  align:'center', title: "剩余应还"},
                {width:200, field:'planRepayTime', unresize: true,  align:'center',templet:'#divlistGmt', title: "应还时间"},
                {width:100, field:'status', unresize: true,  align:'center', templet:'#statusDiv',title: "状态"},
                { width: 300, align:'center', toolbar: '#thirdBarBorrowTable',title: "操作"}
            ]],
        });
        //期数表
        function periodsInfos(data) {
            layui.table.render({
                elem: '#periodsInfoTable',
                url: '/api/user/getStatus.json',
                cellMinWidth: 250,
                height: 300,
                totalRow: true,
                contentType: 'application/json; charset=utf-8',
                method: 'post',
                parseData: function(res){ //res 即为原始返回的数据
                    return {
                        "code": data.code,//解析接口状态
                        "msg": data.msg, //解析提示文本
                        "data": data.data.periodsInfo //解析数据列表
                    };
                },
                response: { statusCode: 100 },
                cols: [[ //表头
                    {width:250, field:'nper',unresize: true,  align:'center',  title: "期数"},
                    {width:300, field:'sumAmount',unresize: true,  align:'center', title: "账单金额"},
                    {width:250, field:'amount',unresize: true,  align:'center',  title: "账单本金"},
                    {width:280, field:'planRepayTime', unresize: true,align:'center', title: "应还时间"},
                    {width:150, field:'repayAmount',unresize: true,  align:'center', title: "入账金额"},
                    {width:150, field:'restAmount', unresize: true,align:'center', title: "剩余应还"},
                    {width:200, field:'status',unresize: true,  align:'center', templet:'#periodStatusDiv', title: "状态"},
                ]],
            });
        }
        //还款记录表
        function repayInfos(data){
            layui.table.render({
                elem: '#repayRecordTable',
                url: '/api/user/getStatus.json',
                cellMinWidth: 250,
                height: 300,
                totalRow: true,
                contentType: 'application/json; charset=utf-8',
                method: 'post',
                parseData: function(res){ //res 即为原始返回的数据
                    return {
                        "code": data.code,//解析接口状态
                        "msg": data.msg, //解析提示文本
                        "data": data.data.repayInfos //解析数据列表
                    };
                },
                response: { statusCode: 100 },
                cols: [[ //表头
                    {width:250, field:'repayId',unresize: true,  align:'center',  title: "还款ID"},
                    {width:300, field:'accountTime',unresize: true,  align:'center', templet:'#accountTimeDiv', title: "入账时间"},
                    {width:250, field:'repayAmount',unresize: true,  align:'center',  title: "还款金额"},
                    {width:280, field:'name', unresize: true,align:'center', title: "提交人"},
                    {width:500, field:'remark',unresize: true,  align:'center', title: "备注"},
                ]],
            });
        }
        //续期记录
        function renewalInfos(data){
            layui.table.render({
                elem: '#reneRecordTable',
                cellMinWidth: 250,
                height: 300,
                totalRow: true,
                loading: false,
                contentType: 'application/json; charset=utf-8',
                url: '/api/user/getStatus.json',
                method: 'post',
                parseData: function(res){ //res 即为原始返回的数据
                    return {
                        "code": data.code,//解析接口状态
                        "msg": data.msg, //解析提示文本
                        // "count": data.renewalCount, //解析数据长度
                        "data": data.data.renewalInfos //解析数据列表
                    };
                },
                response: { statusCode: 100 },
                cols: [[ //表头
                    {width:250, field:'renewalId',unresize: true,  align:'center',  title: "续期Id"},
                    {width:250, field:'renewalAmount',unresize: true,  align:'center',  title: "续期本金"},
                    {width:250, field:'renewalSumAmount',unresize: true,  align:'center',  title: "续期总费用"},
                    {width:250, field:'renewalRepayCapital', unresize: true,align:'center', title: "还款本金"},
                    {width:250, field:'renewalRepayLastAmount',unresize: true,  align:'center', title: "已还上期费用"},
                    {width:350, field:'renewalTime', unresize: true, templet:'#renewalTimeDiv', align:'center', title: "续期时间"},
                ]],
            });
        }
       //退款记录
        function refundInfos(data){
            layui.table.render({
                elem: '#refundTable',
                cellMinWidth: 250,
                height: 200,
                totalRow: true,
                loading: false,
                contentType: 'application/json; charset=utf-8',
                url: '/api/user/getStatus.json',
                method: 'post',
                parseData: function(res){ //res 即为原始返回的数据
                    return {
                        "code": data.code,//解析接口状态
                        "msg": data.msg, //解析提示文本
                        "data": data.data.reFundInfos //解析数据列表
                    };
                },
                response: { statusCode: 100 },
                cols: [[ //表头
                    {width:150, field:'amount',unresize: true,  align:'center',title: "退款金额"},
                    {width:300, field:'operater',unresize: true,  align:'center',  title: "申请人"},
                    {width:280, field:'gmtCreate', unresize: true,align:'center', title: "申请时间"},
                    {width:280, field:'status', unresize: true,align:'center', templet:'#reviewStatus',title: "审核结果"},
                    {width:280, field:'finance', unresize: true,align:'center', title: "审核人"},
                    {width:280, field:'refundTime', unresize: true,align:'center', title: "审核时间"},
                    {width:300, field:'remark',unresize: true,  align:'center',  title: "原因"},
                ]],
            });
        }
        //余额详情
        function amountRecord(){
            layui.table.render({
                elem: '#amountDetailTable',
                cellMinWidth: 200,
                height: 600,
                page: false,
                loading: false,
                totalRow: true,
                contentType: 'application/json; charset=utf-8',
                url: '/api/user/listAccountRecord.json',
                method: 'post',
                where:{mobile: $('#mobile').val()},
                parseData: function(res){ //res 即为原始返回的数据
                    return {
                        "code": res.code,//解析接口状态
                        "msg": res.msg, //解析提示文本
                        "data": res.data //解析数据列表
                    };
                },
                response: { statusCode: 100 },
                cols: [[ //表头
                    {width:200, field:'rid',unresize: true,  align:'center',  title: "记录ID"},
                    {width:300, field:'gmtCreate',unresize: true,  align:'center',  title: "交易时间"},
                    {width:200, field:'amount',unresize: true,  align:'center',title: "金额"},
                    {width:250, field:'type',unresize: true,templet:'#recordTypeDiv',  align:'center',  title: "资金动态"},
                    {width:300, field:'tradeNo', unresize: true,align:'center', title: "交易流水"},
                    {width:150, field:'operator',unresize: true,  align:'center', title: "操作角色"},
                    {width:220, field:'',unresize: true,  align:'center', title: "相关案件"},
                ]],
            });
        }


        function getBorrowInfo(borrowInfo){
            $("#dCompany").val(borrowInfo.company);
            if(borrowInfo.productType=="CONSUMPERIOD"){
                $("#dProductType").val("消费分期");
            }else if(borrowInfo.productType=="CASH"){
                $("#dProductType").val("现金贷");
            }else if(borrowInfo.productType=="CASHPERIOD"){
                $("#dProductType").val("现金分期");
            }
            if(borrowInfo.day=="SEVEN"){
                $("#dDay").val("7");
            }else if(borrowInfo.day=="FOURTEEN"){
                $("#dDay").val("14");
            }else {
                $("#dDay").val(borrowInfo.day);
            }
            $("#dNper").val(borrowInfo.nper);
            $("#dNperAmount").val(borrowInfo.nperAmount);
            $("#dNpered").val(borrowInfo.npered);
            $("#dBorrowNo").val(borrowInfo.orderNo);
            $("#dTradeTime").val(borrowInfo.tradeTime);
            $("#dPlanRepayTime").val(borrowInfo.planRepayTime);
            $("#dAmount").val(borrowInfo.amount);
            $("#dRepayAmount").val(borrowInfo.repayAmount);
            $("#dOverdue").val(borrowInfo.overdueAmount);
            $("#dServiceAmount").val(borrowInfo.serviceAmount);
            $("#dReAmount").val(borrowInfo.restAmount);
            if(borrowInfo.status=="APPLY"){
                $("#dStatus").val("申请/未审核");
            }else if(borrowInfo.status=="WAITTRANSED"){
                $("#dStatus").val("待打款");
            }else if(borrowInfo.status=="TRANSFERING"){
                $("#dStatus").val("打款中");
            }else if(borrowInfo.status=="TRANSFERRED"){
                $("#dStatus").val("已经打款/待还款");
            }else if(borrowInfo.status=="FINISHED"){
                $("#dStatus").val("已结清");
            }else if(borrowInfo.status=="FINSH"){
                $("#dStatus").val("已结清");
            }else if(borrowInfo.status=="TRANSED"){
                $("#dStatus").val("已经打款/待还款");
            }
        }

        //事件监听
        layui.table.on('tool(borrowTable)', function(obj){
            borrowTableClick(obj)
        });
        layui.table.on('tool(thirdBorrowTable)', function(obj){
            borrowTableClick(obj)
        });
        function borrowTableClick(obj){
            var data = obj.data //获得当前行数据
                ,layEvent = obj.event; //获得 lay-event 对应的值
            if(layEvent === 'detail'){
                //展现 商品信息框
                $("#goodsDiv").show();
                //隐藏商品为空的 提示
                $("#goodsIsNull").hide();
                $.ajax({
                    url:"/api/user/getBorrowDetail.json",
                    data:{
                        mobile:$("#mobile").val(),
                        borrowNo:data.orderNo,
                        produce: data.companyId
                    },
                    success:function(result) {
                        //用户信息
                        var data=result.data;
                        var userInfo=data.userInfo;
                        $("#dUserName").val(userInfo.userName);
                        $("#dMobile").val(userInfo.mobile);
                        $("#dIdNumber").val(userInfo.idNumber);
                        //续期头
                        $("#renewalCount").text(data.renewalCount);
                        $("#renewalAllSumAmount").text(data.renewalAllSumAmount);
                        //借款信息
                        getBorrowInfo(data.borrowInfo);
                        //商品信息
                        var goodsInfo=data.goodsInfo;
                        if(JSON.stringify(goodsInfo) === '{}' || goodsInfo == ''){
                            //展现 商品信息框
                            $("#goodsDiv").hide();
                            //隐藏商品为空的 提示
                            $("#goodsIsNull").show();
                        }else {
                            $("#goodsOrderNo").val(goodsInfo.goodsOrderNo);
                            $("#goodsName").val(goodsInfo.goodsName);
                            $("#goodsAmount").val(goodsInfo.goodsAmount);
                            $("#goodsDeliveryMobile").val(goodsInfo.goodsDeliveryMobile);
                            $("#goodsDeliveryAddress").val(goodsInfo.goodsDeliveryAddress);
                            $("#goodsDeliveryUserName").val(goodsInfo.goodsDeliveryUserName);
                        }
                        periodsInfos(result);
                        repayInfos(result);
                        renewalInfos(result);
                        refundInfos(result);
                        layer.open({
                            id:"detail",
                            type: 1,
                            title: "案例",
                            closeBtn: 1,
                            area : [ ($(window).width() * 0.95) + 'px',
                                ($(window).height() * 0.95) + 'px' ],
                            btn: ['取消'],
                            btnAlign: 'c',
                            shadeClose: false,
                            zIndex : layer.zIndex,
                            content: $("#detailDiv"),
                        });
                    },
                    error:function () {
                        layer.msg("请求异常")
                    }
                })

            } else if(layEvent === 'refund'){
                var refundedAmount;
                var restRepay;
                $.ajax({
                    url: "/api/user/refunded.json",
                    data: {
                        borrowNo: data.orderNo,
                    },
                    success: function (result) {
                        refundedAmount=result.data;
                        restRepay=data.restAmount;
                        $("#reUserAmount").text(parseFloat(restRepay)<0?Math.abs(restRepay)-Number(refundedAmount) :0);
                    }
                })

                layer.open({
                    id:"refund",
                    type: 1,
                    title: "退款",
                    area:  [ "500px",
                        "480px" ],
                    btnAlign: 'c',
                    btn:["取消","确定"],
                    content: $("#refundDiv"),
                    btn2:function(){
                        var refundAmount=$("#refundAmount").val();
                        var rufundType=$("#rufundType").val();
                        var refundAccount=$("#refundAccount").val();
                        var refundRemark=$("#refundRemark").val();
                        if(refundAmount=='' || rufundType=='' || refundRemark==''){
                            layer.msg("必选填项不能为空")
                            return;
                        }
                        if(restRepay=='0'){
                            layer.alert("可退金额不足!")
                            return;
                        }
                        $.ajax({
                            url:'/api/user/refund.json', data:{userAmount:$("#reUserAmount").text(),reFundAmount:refundAmount,borrowNo:data.orderNo
                                ,type:rufundType,userAccount:refundAccount,remark:refundRemark,mobile:$("#mobile").val()}, success:function(data){
                                if(data.code==901){
                                    window.location.href="login.html"
                                }
                                if(data.code==100){
                                    $("#refundAmount").val("");
                                    $("#refundAccount").val("");
                                    $("#refundRemark").val("");
                                    layer.alert("退款申请成功,等待财务审核！")
                                    UI.query()
                                }else {
                                    layer.alert(data.msg==null?"退款申请失败":data.msg)
                                }

                            }
                        });
                    }
                })
            } else if(layEvent === 'offlineRepay'){
                $("#offlineRepayIsFinishDiv").hide();
                if(data.status=='FINISHED'){
                    layer.msg("已结清，不能操作余额还款！");
                    return;
                }
                if(data.status!='TRANSFERRED'){
                    layer.msg("不在还款状态，不能操作还款！");
                    return;
                }

                if(data.productType=='CASHPERIOD'){
                    $("#offlineRepayIsFinishDiv").show();
                }

                layer.open({
                    id:"offline",
                    type: 1,
                    title: "线下还款",
                    closeBtn: 1,
                    area : [ "450px","400px" ],
                    btn: ['确定','取消'],
                    btnAlign: 'c',
                    zIndex : layer.zIndex,
                    content: $("#offlineRepayDiv"),
                    btn1:function (index) {
                        var finish="N";
                        if ($("#offlineRepayIsFinish").is(':checked')) {
                            finish="Y";
                        }
                        $.ajax({url:"/api/offline/repay.json",
                            data:{
                                companyId: data.companyId,
                                borrowNo:data.orderNo,
                                repayType:$("#repayType").val(),
                                payTime:$("#payTime").val(),
                                bankCard:$("#bankCard").val(),
                                restAmount:data.restAmount,
                                repayAmount:$("#offlineRepayAmount").val(),
                                repayTradeNo:$("#tradeNo").val(),
                                mobile:$("#mobile").val(),
                                remark:$("#offlineRemark").val(),
                                isAllFinish:finish
                            },
                            success:function(data) {
                                if(data.code==100){
                                    layer.msg("还款成功");
                                }else {
                                    layer.alert(data.msg==''?"还款失败":"还款失败："+data.msg)
                                }
                                $("#offlineRepayIsFinish").checked=false;
                                layer.close(index)
                                UI.query();

                            },
                            error:function () {
                                $("#offlineRepayIsFinish").checked=false;
                                layer.msg("请求异常")
                            }
                        })

                    }
                });
            }else if(layEvent === 'inAccount'){
                $("#inAccountIsFinishDiv").hide();
                if(data.status=='FINISHED'){
                    layer.msg("已结清，不能操作余额入账！");
                    return;
                }
                if(data.status!='TRANSFERRED'){
                    layer.msg("不在还款状态，不能操作入账！");
                    return;
                }
                $("#inAmountAndTradeNo").html('')
                $.ajax({
                    url: "/api/user/tradeNo.json",
                    data: {
                        mobile: $("#mobile").val(),
                    }, success: function (result) {
                        if(result.code==100){
                            var html='';
                            html=html+"<option value=''>请选择入帐金额</option>";
                            for(var i=0;i<result.data.length;i++){
                                html=html+"<option value=''>"+result.data[i].tradeNo+"</option>";
                            }
                            $("#inAmountAndTradeNo").append(html);
                        }else {
                            layer.alert("查询可入账金额失败！")
                        }
                    }
                })
                if(data.productType=="CONSUMPERIOD"){
                    $("#inProductType").val("消费分期");
                }else if(data.productType=="CASH"){
                    $("#inProductType").val("现金贷");
                }else if(data.productType=="CASHPERIOD"){
                    $("#inProductType").val("现金分期");
                    $("#inAccountIsFinishDiv").show();
                }
                if(data.day=="SEVEN"){
                    $("#inBorrowDay").val(7)
                }else if(data.day=="FOURTEEN"){
                    $("#inBorrowDay").val(14);
                }else {
                    $("#inBorrowDay").val(data.day);
                }
                $("#inRepayAmount").val(data.repayAmount);
                $("#inOverdueAmount").val(data.overdueAmount);
                $("#inRestAmount").val(data.restAmount);
                $("#inNoReductionAmount").val(data.noReductionAmount);
                $("#inPlanRepayTime").val(data.planRepayTime);
                layer.open({
                    id:"inAccount",
                    type: 1,
                    title: "入账",
                    closeBtn: 1,
                    area: ['650px', '500px'],
                    shade: 0.8,
                    btn: ['确认入账', '取消入账'],
                    btnAlign: 'c',
                    shadeClose: false,
                    content: $("#inAccountDiv"),
                    btn1:function (index) {
                        var finish="N";
                        if ($("#inAccountIsFinish").is(':checked')) {
                            finish="Y";
                        }
                        var inAccountCause=$("#inAccountCause").val();
                        var inAmountTradeNo=$("#inAmountAndTradeNo option:selected").text();
                        if(inAmountTradeNo=='请选择入帐金额'){
                            layer.alert("请选择入账金额");
                            return;
                        }
                        $.ajax({url:"/api/offline/inAccount.json",
                            data:{
                                borrowNo: data.orderNo,
                                // isBalance:finish,
                                remark:inAccountCause,
                                companyId:data.companyId,
                                isAllFinish:finish,
                                inAmountTradeNo:inAmountTradeNo,
                                mobile:$("#mobile").val()
                            },
                            success:function(data) {
                                if(data.code==100){
                                    $("#inAccountIsFinish").attr("checked",false);
                                    $("#inAmount").val("");
                                    $("#inAccountCause").val("");
                                    layer.alert("入账成功")
                                }else {
                                    layer.alert(data.msg==''?"入账失败":"入账失败："+data.msg)
                                }
                                UI.query();
                                layer.close(index);
                            },
                            error:function () {
                                layer.msg("请求异常")
                            }
                        })
                    },
                });
            }
        }
        // 日期组件初始化
		layui.laydate.render({
		  elem: '#payTime'
		  ,theme: '#01AAED'
		});
		layui.laydate.render({
		  elem: '#dateEnd'
		  ,max: 1
		  ,theme: '#01AAED'
		});

		// 数据上送初始化
		var form = layui.form;

        //查询记录详情
        $('#amountRecordBtn').click(function(){
              amountRecord();
              layer.open({
                    id:"inAccount",
                    type: 1,
                    title: "余额记录详情",
                    closeBtn: 1,
                    area:  [ ($(window).width() * 0.95) + 'px',
                        ($(window).height() * 0.9) + 'px' ],
                    shadeClose: false,
                    content: $("#amountDetailDiv")
                })
        });

        $('#cancelInAccount').click(function(){
            $("#inAccountDiv").parent().parent().hide();
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

        $('#btnSearch').click(function () {
                $.ajax({url:"/api/user/get.json",
                    data:{
                        mobile: $("#mobile").val(),
                    },
                    contentType: 'application/json; charset=utf-8',
                    success:function(data) {
                        if(data.code==901){
                            window.location.href="login.html";
                        }
                        UI.query();
                    },
                    error:function (data) {
                        if(data.responseJSON.code==901){
                            layer.alert('<span style="font-size:18px;">会话超时，需要重新登录!</span>', {icon:6, area:['360px','180px'], btn:['确定']},
                                function(){
                                    layer.closeAll();
                                    window.parent.location.href= '/login.html';
                                });
                        }else {
                            layer.msg("请求异常")
                        }
                    }
                })
                return false;
            });

        $('#btnShowCreate').click(function () {
                UI.showCreatePopup();
                return false;
            });

        $('#btnExport').click(function () {
                // TODO
                return false;
            });
        $('#thirdShow').click(function () {
            $("#selfAccount").hide();
            $("#thirdAccount").show();

        });

        $('#show').click(function () {
            $("#selfAccount").show();
            $("#thirdAccount").hide();

        });

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
        var mobile= $('#mobile').val();
        $("#userInfo").hide();
        layui.table.reload('userTable', {
            where: {
                mobile:mobile,
            }
        });
        $.ajax({url:"/api/user/getAccount.json",data:{mobile: mobile},success:function(data) {
        	   if(data.code==100){
        	   	   $("#userAmount").text(data.data.amount);
                   $("#noFinishSelfCount").text(data.data.noFinishSelfCount)
                   $("#noFinishThirdCount").text(data.data.noFinishThirdCount)
                   $("#selfReceivableAmount").text(data.data.selfReceivableAmount)
                   $("#thirdReceivableAmount").text(data.data.thirdReceivableAmount)
                   $("#selfRepayAmount").text(data.data.selfRepayAmount)
                   $("#thirdRepayAmount").text(data.data.thirdRepayAmount)
                   $("#selfResAmount").text(data.data.selfResAmount)
                   $("#thirdResAmount").text(data.data.thirdResAmount)
                   $("#userInfo").show();
                   layui.table.reload('borrowTable', {
                       page: {
                           curr: 1 //重新从第 1 页开始
                       },
                       where: {
                           mobile:mobile,
                           ascription:"self",
                       }
                   });
                   layui.table.reload('thirdBorrowTable', {
                       page: {
                           curr: 1 //重新从第 1 页开始
                       },
                       where: {
                           mobile:mobile,
                           ascription:"third",
                       }
                   });
               }
         }});


    },
	
	showModifyPopup: function(srcDom){
		// TODO 赋值
		layer.open({
		  type: 1,
		  title: "编辑管理员",
		  closeBtn: 1,
		  area: 'auto',
		  maxWidth: 550,
		  shadeClose: false,
		  content: $('#divModify')
		});
	},
	
	showCreatePopup: function(){
		layer.open({
		  type: 1,
		  title: "新增管理员",
		  closeBtn: 1,
		  area: 'auto',
		  maxWidth: 550,
		  shadeClose: false,
		  content: $('#divCreate')
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
    getLoginStatus: function(callback){
        $.post("/api/login/status.json", function(jsonResp){
            if(callback)callback(jsonResp);
        });
    },
	create : function(data, callback){
		$.post('/api/operatpr/create.json', data, function(jsonResp){
			if(callback)callback(jsonResp);
		});
	},

	modify : function(data, callback){
		$.post('/api/operatpr/modify.json', data, function(jsonResp){
			if(callback)callback(jsonResp);
		});
	}
}

