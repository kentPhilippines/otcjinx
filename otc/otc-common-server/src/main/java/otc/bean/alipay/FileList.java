package otc.bean.alipay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>文件列表</p>
 * @author K
 */
public class FileList implements Serializable{
	private static final long serialVersionUID = 1L;
    private Integer id;								//数据id
    private String fileId;							//文件id
    private String fileholder;						//文件所属人
    private String isFixation;						//是否为固码 1为固定码 2不为固定码
    private BigDecimal fixationAmount;				//若为固码,固码金额
    private String code;							//收款媒介标识:alipay(支付宝),weichar(微信),card(银行卡),other(暂未开放)
    private String concealId;						//关联收款媒介ID
    private Date createTime;
    private Date submitTime;
    private Integer status;
    private String isDeal;							
    private String isCut;							//图片是否以剪裁 Y 剪裁过 字段未空则未剪裁
    private String retain1;
    private String retain2;
    private String fileNote;						//备注
    
    
    
    
    
    
    private String mediumNumber;   					//媒介标识   缓存用
    private String phone;							//二维码手机号
    private String attr;							//媒介顶代code
    
    
    
    
    public String getMediumNumber() {
		return mediumNumber;
	}
	public void setMediumNumber(String mediumNumber) {
		this.mediumNumber = mediumNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
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
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }
    public String getFileholder() {
        return fileholder;
    }
    public void setFileholder(String fileholder) {
        this.fileholder = fileholder == null ? null : fileholder.trim();
    }
    public String getIsFixation() {
        return isFixation;
    }
    public void setIsFixation(String isFixation) {
        this.isFixation = isFixation == null ? null : isFixation.trim();
    }
    public BigDecimal getFixationAmount() {
        return fixationAmount;
    }
    public void setFixationAmount(BigDecimal fixationAmount) {
        this.fixationAmount = fixationAmount;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
    public String getConcealId() {
        return concealId;
    }
    public void setConcealId(String concealId) {
        this.concealId = concealId == null ? null : concealId.trim();
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
    public String getIsCut() {
        return isCut;
    }
    public void setIsCut(String isCut) {
        this.isCut = isCut == null ? null : isCut.trim();
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
    public String getFileNote() {
        return fileNote;
    }
    public void setFileNote(String fileNote) {
        this.fileNote = fileNote == null ? null : fileNote.trim();
    }
	@Override
	public String toString() {
		return "FileList [id=" + id + ", fileId=" + fileId + ", fileholder=" + fileholder + ", isFixation=" + isFixation
				+ ", fixationAmount=" + fixationAmount + ", code=" + code + ", concealId=" + concealId + ", createTime="
				+ createTime + ", submitTime=" + submitTime + ", status=" + status + ", isDeal=" + isDeal + ", isCut="
				+ isCut + ", retain1=" + retain1 + ", retain2=" + retain2 + ", fileNote=" + fileNote + "]";
	}
}