package pay.service.impl;
import org.springframework.stereotype.Component;
import pay.entity.TestBean;
import pay.service.TestService;

@Component
public class TestServiceImpl implements TestService {
	@Override
	public TestBean findTest() {
		TestBean testBean = new TestBean();
		testBean.setTest1("你好我是测试接口");
		testBean.setTest2("今晚打老虎");
		testBean.setTest3("");
		return testBean;
	}

}
