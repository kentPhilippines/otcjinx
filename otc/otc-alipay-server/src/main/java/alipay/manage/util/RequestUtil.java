package alipay.manage.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.util.StrUtil;
import otc.result.Result;
import otc.util.encode.XRsa;

/**
 * <p>请求参数加解密工具类</p>
 * @author kent
 */
public class RequestUtil {
	static Logger log = LoggerFactory.getLogger(RequestUtil.class);
	private static String PrivateKey;
	private static String PublicKey;
	private static XRsa rsa;
	private static final String PUBLIC_KEY  = "publicKey";
	private static final String PRIVATE_KEY  = "privateKey";
	static {
		init();
	}
	static void init(){
		Map<String, String> createKeys = XRsa.createKeys(512);
		PrivateKey = createKeys.get(PUBLIC_KEY);
		PublicKey = createKeys.get(PRIVATE_KEY);
	}
	static String getPublicKey(){
		return PublicKey;
	}
	
	static String getPrivateKey() {
		return PrivateKey;
	}
	static void initRequest(){
		rsa = new XRsa(PublicKey, PrivateKey);
	}
	/**
	 * <p>公钥加密</p>
	 * @param param			加密参数
	 * @return
	 */
	public Result publicEncrypt(String param){
		if(StrUtil.isBlank(param))
			return Result.buildFailMessage("加密参数为空");
		String publicEncrypt = rsa.publicEncrypt(param);
		log.info("加密密参数为："+param);
		log.info("加密后的值为："+publicEncrypt);
		return Result.buildSuccessResult(publicEncrypt);
	}
	
	/**
	 * <p>私钥加密</p>
	 * @param param			加密参数
	 * @return
	 */
	public Result privateEncrypt(String param) {
		if(StrUtil.isBlank(param))
			return Result.buildFailMessage("加密参数为空");
		String publicEncrypt = rsa.privateEncrypt(param);
		log.info("加密密参数为："+param);
		log.info("加密后的值为："+publicEncrypt);
		return Result.buildSuccessResult(publicEncrypt);
	}
	
	
	
	public Result privateDecrypt(String param) {
		if(StrUtil.isBlank(param))
			return Result.buildFailMessage("解密参数为空");
		String privateDecrypt = rsa.privateDecrypt(param);
		log.info("解密参数为："+param);
		log.info("解密后的值为："+privateDecrypt);
		return Result.buildSuccessResult(privateDecrypt);
	}
	
	
	
	
	
}
