package alipay.manage.bean;

import java.util.Date;

public class ExceptionOrder {
    private Integer id;

    private String orderExceptId;

    private String orderId;

    private Integer exceptStatus;

    private Integer exceptType;

    private String orderAccount;

    private String exceptOrderAmount;

    private String orderGenerationIp;

    private String operation;

    private Date createTime;

    private Date submitTime;

    private String submitSystem;

    private Integer status;

    private String retain1;

    private String retain2;

    private String retain3;

    private String explains;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderExceptId() {
        return orderExceptId;
    }

    public void setOrderExceptId(String orderExceptId) {
        this.orderExceptId = orderExceptId == null ? null : orderExceptId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getExceptStatus() {
        return exceptStatus;
    }

    public void setExceptStatus(Integer exceptStatus) {
        this.exceptStatus = exceptStatus;
    }

    public Integer getExceptType() {
        return exceptType;
    }

    public void setExceptType(Integer exceptType) {
        this.exceptType = exceptType;
    }

    public String getOrderAccount() {
        return orderAccount;
    }

    public void setOrderAccount(String orderAccount) {
        this.orderAccount = orderAccount == null ? null : orderAccount.trim();
    }

    public String getExceptOrderAmount() {
        return exceptOrderAmount;
    }

    public void setExceptOrderAmount(String exceptOrderAmount) {
        this.exceptOrderAmount = exceptOrderAmount == null ? null : exceptOrderAmount.trim();
    }

    public String getOrderGenerationIp() {
        return orderGenerationIp;
    }

    public void setOrderGenerationIp(String orderGenerationIp) {
        this.orderGenerationIp = orderGenerationIp == null ? null : orderGenerationIp.trim();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation == null ? null : operation.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitSystem() {
        return submitSystem;
    }

    public void setSubmitSystem(String submitSystem) {
        this.submitSystem = submitSystem == null ? null : submitSystem.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRetain1() {
        return retain1;
    }

    public void setRetain1(String retain1) {
        this.retain1 = retain1 == null ? null : retain1.trim();
    }

    public String getRetain2() {
        return retain2;
    }

    public void setRetain2(String retain2) {
        this.retain2 = retain2 == null ? null : retain2.trim();
    }

    public String getRetain3() {
        return retain3;
    }

    public void setRetain3(String retain3) {
        this.retain3 = retain3 == null ? null : retain3.trim();
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains == null ? null : explains.trim();
    }
}