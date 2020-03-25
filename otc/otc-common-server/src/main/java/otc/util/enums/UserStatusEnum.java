package otc.util.enums;

import com.google.common.collect.Maps;
import otc.exception.BusinessException;

import java.util.Map;

public enum UserStatusEnum {

    OPEN(1, "启用"),
    CLOSE(0, "停用");

    private Integer code;
    private String desc;

    UserStatusEnum(Integer code, String desc) {
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

    private static final Map<Integer, UserStatusEnum> HOLDER = Maps.newHashMap();

    static {
        for (UserStatusEnum value : UserStatusEnum.values()) {
            HOLDER.put(value.code, value);
        }
    }

    public static UserStatusEnum resolve(Integer code) {
        if (HOLDER.containsKey(code)) {
            return HOLDER.get(code);
        }
        throw new BusinessException("参数错误");
    }

    public boolean matches(Integer code) {
        return this.getCode() == code;
    }

}

