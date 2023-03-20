package test.number.channal;

public enum PayEnum {
    ERR_0001("0001", "商户签名异常"),

    ERR_0010("0010", "系统错误"),
    ERR_0011("0011", "请使用post方法"),
    ERR_0012("0012", "post数据为空"),
    ERR_0013("0013", "签名错误"),
    ERR_0014("0014", "参数错误"),
    ERR_0015("0015", "商户不存在"),
    ERR_0020("0020", "系统错误1"),
    ERR_0021("0021", "系统错误2"),
    ERR_0022("0022", "系统错误3"),
    ERR_0023("0023", "系统错误4"),
    ERR_0024("0024", "系统错误5"),
    ERR_0110("0110", "第三方超时"),
    ERR_0111("0111", "第三方异常"),
    ERR_0112("0112", "订单不存在"),
    ERR_0113("0113", "订单已支付"),
    ERR_0114("0114", "商品不存在"),
    ERR_0115("0115", "价格不对"),
    ERR_0116("0116", "物品数量不对"),
    ERR_0117("0117", "过程返回255"),
    ERR_0118("0118", "DB错误"),
    ERR_0119("0119", "支付中心没有对应的渠道"),
    ERR_0120("0120", "修改订单状态失败"),
    ERR_0121("0121", "无可用银行卡，请联系客服！"),
    ERR_0122("0122", "银行卡与实名信息不匹配"),
    ERR_0123("0123", "银行卡在黑名单内"),
    ERR_0124("0124", "付款人填写次数过多"),
    ERR_0125("0125", "商户系统网银通道费率未设置"),
    ERR_0126("0126", "商户USDTOMNI通道费率未设置");

    private String code;
    private String message;

    PayEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
