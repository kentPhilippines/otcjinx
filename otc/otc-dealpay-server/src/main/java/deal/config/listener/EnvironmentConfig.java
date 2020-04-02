package deal.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvironmentConfig implements EnvironmentAware{
	Logger log = LoggerFactory.getLogger(EnvironmentConfig.class);
	@Override
	public void setEnvironment(Environment environment) {
		log.info("项目启动正常，初始化完成");
	}
}
