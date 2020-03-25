package alipay.manage.api.Feign;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import alipay.manage.api.Feign.impl.FileServiceClienFeignHystrix;
import otc.common.PayApiConstant;

@FeignClient(value=PayApiConstant.Server.FILE_SERVER, fallback = FileServiceClienFeignHystrix.class )
public interface FileServiceClienFeign {
	@PostMapping( PayApiConstant.File.FILE_API+PayApiConstant.File.ADD_FILE)
	public String addFile(@RequestParam("file")String file);
	@PostMapping( PayApiConstant.File.FILE_API+PayApiConstant.File.FIND_FILE)
	public Resource loadAsResource(@RequestParam("id")String id);
}
