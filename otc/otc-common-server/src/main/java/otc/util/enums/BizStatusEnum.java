package otc.util.enums;

import cn.hutool.core.map.MapUtil;
import otc.exception.BusinessException;

import java.util.Map;

/**
 * 接单状态，代付状态
 */
public enum BizStatusEnum {

    BIZ_STATUS_OPEN(1, "启用"),
    BIZ_STATUS_CLOSE(2, "停用");

    private Integer code;
    private String desc;

    BizStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private static final Map<Integer, BizStatusEnum> HOLDER = MapUtil.newHashMap();

    static {
        for (BizStatusEnum value : BizStatusEnum.values()) {
            HOLDER.put(value.code, value);
        }
    }

    public static BizStatusEnum resolve(Integer code) {
        if (HOLDER.containsKey(code)) {
            return HOLDER.get(code);
        }
        throw new BusinessException("参数错误");
    }

    public boolean matches(Integer code) {
        return this.getCode() == code;
    }
}
