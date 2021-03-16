package alipay.config.redis;

import cn.hutool.core.thread.ThreadUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;


@Aspect
@Component
public class ServerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAspect.class);
    private static final ExpressionParser PARSER = new SpelExpressionParser();
    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    @Resource
    private RedisCache cache;


    @Around(value = "@annotation(redisLock)")
    public Object handleRedisLock(ProceedingJoinPoint call, RedisLock redisLock) {
        Signature signature = call.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String extraKey = this.getSpELRealVal(call, redisLock.extraKey());
        long waitTime = redisLock.waitTime();
        String lock = className + ":" + methodName + ":" + extraKey;
        LOGGER.info("当前锁：{}", lock);
        // 获取锁
        boolean tryLock = cache.lock(lock);
        LOGGER.info("{}锁获取状态：{} ", lock, tryLock);
        // 不中断
        if (0 >= waitTime) {
            while (!tryLock) {
                // 获取锁
                tryLock = cache.lock(lock);
            }
        } else {
            long start = System.currentTimeMillis();
            while (!tryLock) {
                // 获取锁
                tryLock = cache.lock(lock);
                // 超时中断
                if (System.currentTimeMillis() - start > waitTime) {
                    break;
                }
            }
        }
        if (tryLock) {
            try {
                return call.proceed();
            } catch (Throwable e) {
                LOGGER.error("执行方法{}.{}报错，{}", className, methodName, e.getMessage());
                //  throw e;
            } finally {
                cache.unlock(lock);
            }
        }
        //  throw new OrderException("服务忙，请稍后再试", null);
        return null;
    }

    private String getSpELRealVal(ProceedingJoinPoint call, String spEL) {
        Method method = ((MethodSignature) call.getSignature()).getMethod();
        String[] params = DISCOVERER.getParameterNames(method);
        if (null == params) {
            return spEL;
        }
        Object[] args = call.getArgs();
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(params[i], args[i]);
        }
        Expression expression = PARSER.parseExpression(spEL);
        return expression.getValue(context, String.class);
    }


    void redisLock(String lock) {
        LOGGER.info("当前锁：{}", lock);
        boolean tryLock = cache.lock(lock);
        LOGGER.info("{}锁获取状态：{} ", lock, tryLock);
        while (!tryLock) {
            // 获取锁
            ThreadUtil.sleep(20);
            tryLock = cache.lock(lock);
        }

    }
}
