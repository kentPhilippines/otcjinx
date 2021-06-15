package alipay.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.CountDownLatch;

@Configuration
public class RedisMessageListener {
    /**
     * 创建连接工厂
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter,
                                                   MessageListenerAdapter listenerAdapterTest2,
                                                   MessageListenerAdapter listenerTaskOrder
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //接受消息的key
        container.addMessageListener(listenerAdapter, new PatternTopic("order-deal"));
        container.addMessageListener(listenerAdapterTest2, new PatternTopic("order-wit"));
      //  container.addMessageListener(listenerTaskOrder, new PatternTopic("task-order"));
        return container;
    }


    /**
     * 绑定消息监听者和接收监听的方法
     *
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(ReceiverRedisMessage receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 绑定消息监听者和接收监听的方法
     *
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapterTest2(ReceiverRedisMessage receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage2");
    }
    /**
     * 本意作为当前结算队列,但是现在时间不够,目前先不做优化
     *
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerTaskOrder(ReceiverRedisMessage receiver) {
        return new MessageListenerAdapter(receiver, "taskOrder");
    }

    /**
     * 注册订阅者
     *
     * @param latch
     * @return
     */
    @Bean
    ReceiverRedisMessage receiver(CountDownLatch latch) {
        return new ReceiverRedisMessage(latch);
    }


    /**
     * 计数器，用来控制线程
     *
     * @return
     */
    @Bean
    public CountDownLatch latch() {
        return new CountDownLatch(1);//指定了计数的次数 1
    }
}
