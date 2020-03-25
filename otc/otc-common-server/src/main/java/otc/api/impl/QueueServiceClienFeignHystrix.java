package otc.api.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import otc.api.QueueServiceClienFeign;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.result.Result;
@Component
public class QueueServiceClienFeignHystrix implements QueueServiceClienFeign {

	@Override
	public List<String> getQueue(String[] code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updataNode(String mediumNumber, FileList file) {
		// TODO Auto-generated method stub

	}

	@Override
	public Result addNode(Medium medium) {
		// TODO Auto-generated method stub
		return null;
	}

}
