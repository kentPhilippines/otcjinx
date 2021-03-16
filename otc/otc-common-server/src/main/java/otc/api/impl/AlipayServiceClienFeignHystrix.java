package otc.api.impl;

import org.springframework.stereotype.Component;
import otc.api.AlipayServiceClienFeign;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.result.Result;

import java.util.HashMap;
import java.util.List;
@Component
public class AlipayServiceClienFeignHystrix implements AlipayServiceClienFeign {

	@Override
	public List<Medium> findIsDealMedium(String mediumType, String code) {
		return null;
	}

	@Override
	public List<Medium> findIsDealMedium(String mediumAlipay) {
		return null;
	}

	@Override
	public Result offMediumQueue(String mediumNumber) {
		return null;
	}

	@Override
	public List<FileList> findFileNotCut() {
		return null;
	}

	@Override
	public void updateFileNotDeal(String fileId) {
	}

	@Override
	public void updataFileIsDeal(String fileId) {
	}


	@Override
	public Result enterOrder(HashMap<String, Object> paramMap) {
		return null;
	}

	@Override
	public Result userTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result orderTask() {
		return null;
	}

}
