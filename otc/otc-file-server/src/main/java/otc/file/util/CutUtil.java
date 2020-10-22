package otc.file.util;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.alipay.FileList;
import otc.bean.config.ConfigFile;
import otc.file.feign.AlipayServiceClien;
import otc.file.feign.ConfigServiceClient;
import otc.result.Result;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class CutUtil {
	private static final Log log = LogFactory.get();
	@Autowired
	ConfigServiceClient configServiceClientFeignImpl;
	@Autowired
	AlipayServiceClien alipayServiceClienImpl;
	private static final String FORMAT_NAME = "jpeg";

	public void cut() {
		log.info("开始裁剪图片");
		Result config = configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.LOCAL_STORAGE_PATH);
		String localStoragePath = config.getResult().toString();
		List<FileList> qrList = alipayServiceClienImpl.findFileNotCut();//查询未剪裁的二维码图片
		List<String> codeList = new ArrayList<String>();
		for(FileList code : qrList)
			codeList.add(code.getFileId());
		if(qrList.size()==0 ) 
			return ;  
		for( int i = 0 ; i<30&& i< codeList.size() ; i++  ) {
				String imgName = codeList.get(i);
				Path file = Paths.get(localStoragePath).resolve(imgName);
				File file2 = file.toFile();
				log.info("上传文件长度："+file2.length()); 
				if(!file2.exists() || 0 == file2.length() ) {
					alipayServiceClienImpl.updateFileNotDeal(imgName);//删除不合格二维码
					log.info("图片裁剪标记成功，标记图片的大小为0");
					 continue;
				}
				File f = new File(configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.LOCAL_STORAGE_PATH_BAK).getResult().toString()+imgName);
				if(!f.exists()){
					f.getParentFile().mkdir(); 
					try { //创建文件
					 f.createNewFile(); 
					 } catch (IOException e) { 
						 e.printStackTrace(); 
						 } 
					} 
				String decode = "";
				try {
					 decode = QrCodeUtil.decode(file2);
					 log.info("识别出的二维码连接："+decode);
					 encode(decode,configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.LOCAL_STORAGE_PATH_BAK).getResult().toString(), imgName);
				} catch (Exception e){
					try {
						String readQrCode = readQrCode(file2);
						encode(readQrCode, configServiceClientFeignImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.LOCAL_STORAGE_PATH_BAK).getResult().toString(), imgName);
					} catch (Exception e1) {
						log.info("识别二维码发生异常");
						alipayServiceClienImpl.updateFileNotDeal(imgName);
						log.info("图片裁剪标记成功，标记图片的大小为0");
					}
				} 
			 log.info("裁剪图片id："+imgName);
			  alipayServiceClienImpl.updataFileIsDeal(imgName);
		}
		log.info("剪裁完成");

	}
	public static void encode(String content,  String destPath, String fileName) throws Exception {
		BufferedImage generate = QrCodeUtil.generate(content, QrConfig.create()  );
		mkdirs(destPath);
		String file = fileName + "";//生成随机文件名
			ImageIO.write(generate, FORMAT_NAME, new File(destPath + "/" + file));
		log.info("-------------------------图片生产成功-------------------------"+destPath + "/" + file);
}

public static void mkdirs(String destPath) {
	File file = new File(destPath);
	// 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir。(mkdir如果父目录不存在则会抛出异常)
	if (!file.exists() && !file.isDirectory()) {
		file.mkdirs();
	}
}

public static String readQrCode(File file) throws IOException {  //图片路径
	//读取图片到缓冲区
   /* BufferedImage bufferedImage = ImageIO.read(file);
    //QRCode解码器
    QRCodeDecoder codeDecoder = new QRCodeDecoder();
    *//**
	 *codeDecoder.decode(new MyQRCodeImage())
	 *这里需要实现QRCodeImage接口，移步最后一段代码
	 *//*
    //通过解析二维码获得信息
    String result = new String(codeDecoder.decode(new MyQRCodeImage(bufferedImage)), "utf-8");*/
	return "";
}
}

/*

class MyQRCodeImage implements QRCodeImage{
    BufferedImage bufferedImage;
    public MyQRCodeImage(BufferedImage bufferedImage){
        this.bufferedImage=bufferedImage;
    }
    //宽
    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }
    //高
    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }
    //像素还是颜色
    @Override
    public int getPixel(int i, int j) {
        return bufferedImage.getRGB(i,j);
    }
}
*/
