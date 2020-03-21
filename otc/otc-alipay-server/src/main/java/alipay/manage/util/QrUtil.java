package alipay.manage.util;

import alipay.config.redis.RedisUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;

import javax.annotation.Resource;
import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
@Component
public class QrUtil {
    Logger log = LoggerFactory.getLogger(QrUtil.class);
    @Autowired
    SettingFile settingFile;
    @Resource
    RedisUtil redisUtil;


    /**
     * <p>
     * 输入用户id，查询用户的虚拟冻结金额
     * </p>
     * @param userId 用户id
     * @return amount 当前用户缓存冻结金额
     */
    public BigDecimal getUserAmount(String userId) {
        BigDecimal amount = new BigDecimal("0");
        Map<Object, Object> hmget = redisUtil.hmget(userId);// 用户的虚拟hash金额缓存 key = 用户 + 时间 value = 金额
        Set<Object> keySet = hmget.keySet();
        try {
            for (Object obj : keySet) {
                String accountId = userId;
                int length = accountId.length();
                String subSuf = StrUtil.subSuf(obj.toString(), length);// 时间戳
                Date parse = formatter.parse(subSuf);
                Object object = hmget.get(obj.toString());// 当前金额
                if (!DateUtil.isExpired(parse, DateField.SECOND,
                        Integer.valueOf(settingFile.getName(settingFile.FREEZE_PLAIN_VIRTUAL)), new Date()))
                    redisUtil.hdel(userId, obj.toString());
            }
            Map<Object, Object> hmget2 = redisUtil.hmget(userId);
            Set<Object> keySet2 = hmget2.keySet();
            for (Object obj : keySet2) {
                Object object = hmget2.get(obj.toString());
                BigDecimal money = new BigDecimal(object.toString());
                amount = amount.add(money);
            }
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        return amount;
    }
    DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); //账户未登录错误
}
