package alipay.manage.bean;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * bankconfig对象 alipay_bank_config
 *
 * @author ruoyi
 * @date 2022-07-07
 */
@Data
public class AlipayBankConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date createTime;
    /**
     * id
     */
    private Integer id;

    /**
     * 银行编码
     */
    private String codeValue;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 方言1
     */
    private String alias1;

    /**
     * 方言2
     */
    private String alias2;

    /**
     * 方言3
     */
    private String alias3;

    /**
     * 方言4
     */
    private String alias4;

    

   public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    public void setCodeValue(String codeValue) 
    {
        this.codeValue = codeValue;
    }

    public String getCodeValue() 
    {
        return codeValue;
    }
    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }
    public void setAlias1(String alias1) 
    {
        this.alias1 = alias1;
    }

    public String getAlias1() 
    {
        return alias1;
    }
    public void setAlias2(String alias2) 
    {
        this.alias2 = alias2;
    }

    public String getAlias2() 
    {
        return alias2;
    }
    public void setAlias3(String alias3) 
    {
        this.alias3 = alias3;
    }

    public String getAlias3() 
    {
        return alias3;
    }
    public void setAlias4(String alias4) 
    {
        this.alias4 = alias4;
    }

    public String getAlias4() 
    {
        return alias4;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("codeValue", getCodeValue())
            .append("bankName", getBankName())
            .append("alias1", getAlias1())
            .append("alias2", getAlias2())
            .append("alias3", getAlias3())
            .append("alias4", getAlias4())
            .toString();
    }
}
