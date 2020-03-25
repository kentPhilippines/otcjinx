package otc.file.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import otc.bean.config.ConfigFile;
import otc.file.redis.RedisUtil;
import otc.file.service.ConfigServiceClientFeign;
import otc.result.Result;
import otc.util.number.Number;

@Component
public class StorageUtil {
	Logger log = LoggerFactory.getLogger(StorageUtil.class);
	 @Autowired ConfigServiceClientFeign configServiceClientFeignImpl;
	 @Autowired Base64Utils base64Utils;
	 @Autowired RedisUtil redisUtil;
	 private static final String IMGTYPE = ".jpg";
	 public String uploadGatheringCode(String key) {
	        	String img = Number.getImg();
	            Result config = configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.LOCAL_STORAGE_PATH);
	            String path = config.getResult().toString();
	            log.info(" localStoragePath ::::  "+path);
	        //    Files.copy(inputStream, Paths.get(string).resolve(img), StandardCopyOption.REPLACE_EXISTING);
	            Object object = redisUtil.get(key);
	        	boolean base64ToImage = Base64Utils.Base64ToImage(object.toString(), path+"/"+img);
	        	if(base64ToImage) {
	        		redisUtil.del(key);
	        		return  img;
	        	}
	        	return "";
	    }
}
