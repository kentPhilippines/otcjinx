package alipay.manage.api;

import alipay.config.redis.RedisUtil;
import alipay.manage.util.FtpImgUtil;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import otc.api.FileServiceClienFeign;
import otc.result.Result;
import sun.misc.BASE64Encoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SuppressWarnings({ "unused", "restriction" })
@Controller
@RequestMapping("/storage")
public class StorageApi {
    Logger log = LoggerFactory.getLogger(StorageApi.class);
    @Autowired FileServiceClienFeign fileServiceClienFeignImpl;
    @Autowired RedisUtil redisUtil;
    @PostMapping("/uploadPic")
    @ResponseBody
    public Result uploadPic(@RequestParam("file_data") MultipartFile[] files) throws IOException {
        log.info("上传图片");
        if (ObjectUtil.isNull(files)) {
            return Result.buildFailResult("请选择要上传的图片");
        }
        List<String> storageIds = new ArrayList<>();
        for (MultipartFile file : files) {
        	String addFile = addFile(file);
            storageIds.add(addFile);
        }
        return Result.buildSuccessResult(storageIds);
    }
    
    /**
	 * <p>查看图片接口</p>
	 * @param id
	 * @return
	 */
	@GetMapping("/fetch/{id:.+}")
	public ResponseEntity<Resource> fetch(@PathVariable String id) {
	String fileType = "image/jpeg";
	MediaType mediaType = MediaType.parseMediaType(fileType);
	Resource file = fileServiceClienFeignImpl.loadAsResource(id);
	log.info("【查看图片id："+id+"】");
	if (file == null) {
		file = fileServiceClienFeignImpl.loadAsResource(id);
		if(file == null) 
			return ResponseEntity.notFound().build();
	}
	return ResponseEntity.ok().contentType(mediaType).body(file);
	}
    
    
    
	String addFile(MultipartFile file) {
	    try {
	    	InputStream inputStream = file.getInputStream(); 
			long size = file.getSize();
			String contentType = file.getContentType(); 
			String originalFilename = file.getOriginalFilename();
			byte[] data = null;
			log.info("【文件流："+inputStream+"】");
			log.info("【文件长度："+size+"】");
			log.info("【文件类型："+contentType+"】");
			log.info("【文件名字："+originalFilename+"】");
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
			BASE64Encoder encoder = new BASE64Encoder();
			String encode = encoder.encode(Objects.requireNonNull(data));
			redisUtil.set(originalFilename, encode);
	        String storageId = fileServiceClienFeignImpl.addFile(originalFilename) ;
	        log.info("storageId ::: " + storageId);
	        return storageId;
		    } catch (IOException e) {
		    	return "失败";
			}
    }
    
    
}
