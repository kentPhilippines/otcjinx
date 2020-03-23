package otc.bean.alipay;

import java.util.Date;

/**
 * <p>收款媒介</p>
 * @author K
 */
public class Medium {
    private Integer id;							//数据id
    private String mediumNumber;				//媒介账号
    private String mediumId;					//系统媒介编号
    private String mediumHolder;				//媒介所属人
    private String mediumPhone;					//媒介绑定手机号
    private String qrcodeId;					//媒介所属人
    private String code;						//媒介code
    private Date createTime;
    private Date submitTime;
    private Integer status;
    private String isDeal;
    private String mediumNote;					//媒介备注
    private String attr;   						//收款媒介供应链标识
    public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMediumNumber() {
        return mediumNumber;
    }

    public void setMediumNumber(String mediumNumber) {
        this.mediumNumber = mediumNumber == null ? null : mediumNumber.trim();
    }

    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId == null ? null : mediumId.trim();
    }

    public String getMediumHolder() {
        return mediumHolder;
    }

    public void setMediumHolder(String mediumHolder) {
        this.mediumHolder = mediumHolder == null ? null : mediumHolder.trim();
    }

    public String getMediumPhone() {
        return mediumPhone;
    }

    public void setMediumPhone(String mediumPhone) {
        this.mediumPhone = mediumPhone == null ? null : mediumPhone.trim();
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId == null ? null : qrcodeId.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
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

    public String getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(String isDeal) {
        this.isDeal = isDeal == null ? null : isDeal.trim();
    }

    public String getMediumNote() {
        return mediumNote;
    }

    public void setMediumNote(String mediumNote) {
        this.mediumNote = mediumNote == null ? null : mediumNote.trim();
    }

	@Override
	public String toString() {
		return "Medium [id=" + id + ", mediumNumber=" + mediumNumber + ", mediumId=" + mediumId + ", mediumHolder="
				+ mediumHolder + ", mediumPhone=" + mediumPhone + ", qrcodeId=" + qrcodeId + ", code=" + code
				+ ", createTime=" + createTime + ", submitTime=" + submitTime + ", status=" + status + ", isDeal="
				+ isDeal + ", mediumNote=" + mediumNote + "]";
	}
}