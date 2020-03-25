package otc.api.impl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import otc.api.FileServiceClienFeign;
@Component
public class FileServiceClienFeignHystrix  implements FileServiceClienFeign{

	@Override
	public String addFile(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource loadAsResource(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
