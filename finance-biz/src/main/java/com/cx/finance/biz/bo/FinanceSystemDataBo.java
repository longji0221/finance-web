package com.cx.finance.biz.bo;

import java.util.HashMap;

/**
 * @类描述:清结算系统平台请求bo
 * @author fanmanfu 创建时间：2018年11月02日 下午16:07:41
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */


public class FinanceSystemDataBo extends HashMap<String,String> {

    private static final long serialVersionUID = -6891063734366973441L;
    private String sign;//MD5签名,对data的json串签名
    private String timestamp;
    private String dataType;//数据类型
    private String data;//数据集合
    private String code;
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
        this.put("sign",sign);
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        this.put("timestamp",timestamp);
    }
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
        this.put("dataType",dataType);
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
        this.put("data",data);
    }

    @Override
    public String toString() {
        return "FinanceSystemDataBo [code=" + code +"msg=" + msg +"sign=" + sign + ", timestamp="
                + timestamp + ", dataType=" + dataType + ", data=" + data
                + ", channel=" + "]";
    }

}
