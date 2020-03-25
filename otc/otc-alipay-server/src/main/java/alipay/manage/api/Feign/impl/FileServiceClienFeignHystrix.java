package alipay.manage.api.Feign.impl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import alipay.manage.api.Feign.FileServiceClienFeign;
@Component
public class FileServiceClienFeignHystrix implements FileServiceClienFeign{

	@Override
	public String addFile(String file) {
		return "232";
	}

	@Override
	public Resource loadAsResource(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}
