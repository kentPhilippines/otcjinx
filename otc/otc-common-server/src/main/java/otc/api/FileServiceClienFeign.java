package otc.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import otc.api.impl.FileServiceClienFeignHystrix;
import otc.common.PayApiConstant;

/**
 * <p>全局图片处理</p>
 * @author K
 */
@FeignClient(value=PayApiConstant.Server.FILE_SERVER, fallback = FileServiceClienFeignHystrix.class )
public interface FileServiceClienFeign {
	/**
	 * <p>储存文件</p>
	 * @param file			文件在redis中的存储键 【在调用该方法的时候必须要将图片数据转化为base64并将图片名存入redis   key= 图片名 value = 图片base64值】
	 * @return				【文件存储成功后的图片名】
	 */
	@PostMapping( PayApiConstant.File.FILE_API+PayApiConstant.File.ADD_FILE)
	public String addFile(@RequestParam("file")String file);
	/**
	 * <p>查看文件</p>
	 * @param id							文件id
	 * @return
	 */
	@PostMapping( PayApiConstant.File.FILE_API+PayApiConstant.File.FIND_FILE)
	public Resource loadAsResource(@RequestParam("id")String id);
}

