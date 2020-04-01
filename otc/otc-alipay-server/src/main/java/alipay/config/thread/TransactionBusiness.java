package alipay.config.thread;
import org.springframework.transaction.PlatformTransactionManager;
/**
 * 事务中的业务【如果这个接口是单例可以直接注入  ，就很快了，但是 清除之前的事务链接和线程 是个问题】
 */
public interface TransactionBusiness<T> {
    /**
     * 获取PlatformTransactionManager对象
     * @return
     */
    PlatformTransactionManager getPlatformTransactionManager();

    /**
     * 获取ThreadConnection对象
     * @return
     */
    ThreadConnection getThreadConnection();

    /**
     * 执行业务
     */
    T doInTransaction();

}
