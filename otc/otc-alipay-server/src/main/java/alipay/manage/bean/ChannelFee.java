package alipay.manage.bean;

import java.io.Serializable;
import java.util.Date;

public class ChannelFee implements Serializable{
	/*
	id				int(32) 		NOT NULL		数据id
	channelId		varchar(24) 	NOT NULL		渠道ID
	productId		varchar(24) 	NOT NULL		产品id
	impl			var			char(32) 		NOT NULL		实体类对应关系
	createTime		datetime 		NOT NULL
	submitTime		datetime 		NOT NULL
	statusint(3) 					NOT NULL1						为可用 2为不可用
	*/
	private Integer id;					//数据id
    private Date createTime;
    private Date submitTime;
    private Integer status;
    private String channelRFee;
    private String channelDFee;

    public void setChannelRFee(String channelRFee) {
        this.channelRFee = channelRFee;
    }

    public String getChannelRFee() {
        return this.channelRFee;
    }

    ;

    public void setChannelDFee(String channelDFee) {
        this.channelDFee = channelDFee;
    }

    public String getChannelDFee() {
        return this.channelDFee;
    }

    private String channelId;
    private String productId;
    private String impl;
    private String channelNo;

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getImpl() {
		return impl;
	}
	public void setImpl(String impl) {
		this.impl = impl;
	}
}
