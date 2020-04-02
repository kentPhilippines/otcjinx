package deal.manage.bean;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OrderStatus {
    private Integer id;

    private String orderId;

    private String orderStatusApp;

    private String orderStatusCard;

    private Date createTime;

    private Date submitTime;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderStatusApp() {
        return orderStatusApp;
    }

    public void setOrderStatusApp(String orderStatusApp) {
        this.orderStatusApp = orderStatusApp == null ? null : orderStatusApp.trim();
    }

    public String getOrderStatusCard() {
        return orderStatusCard;
    }

    public void setOrderStatusCard(String orderStatusCard) {
        this.orderStatusCard = orderStatusCard == null ? null : orderStatusCard.trim();
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}