package alipay.manage.api.Feign.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import alipay.manage.api.Feign.QueueServiceClienFeign;
import otc.result.Result;
@Component
public class QueueServiceClienFeignHystrix implements QueueServiceClienFeign{
	@Override
	public List<String> getQueue(String[] code) {
		return new ArrayList<String>();
	}
}
