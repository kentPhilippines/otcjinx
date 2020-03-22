package alipay.manage.util;
import alipay.config.exception.OtherErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class StorageUtil {
    @Autowired
    SettingFile settingFile;
    @Autowired
    FtpImgUtil ftpImgUtil;
    public String uploadGatheringCode(InputStream inputStream, Long fileSize, String fileType, String fileName) {
        if (!fileType.startsWith("image/"))
            throw new OtherErrors("只能上传图片类型的二维码");
        String id = Number.getImg();
        System.out.println("id ::: " + id);
        try {
            String localStoragePath = settingFile.getName("localStoragePath");
            System.out.println(" localStoragePath ::::  "+localStoragePath);
            Files.copy(inputStream, Paths.get("D:\\otherPath\\image").resolve(id), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + id, e);
        }
        return id;
    }
	/*
	public String uploadGatheringCode(InputStream inputStream, Long fileSize, String fileType, String fileName) {
		if (!fileType.startsWith("image/")) {
			throw new OtherErrors("只能上传图片类型的二维码");
		}
		String id = Number.getImg();
		String localStoragePath = settingFile.getName(settingFile.LOCALSTORAGEPATH);
		System.out.println(localStoragePath);
		boolean uploadFile = ftpImgUtil.uploadFile(id, inputStream);
		if(uploadFile)
			return id;
		return null;
	}
	*/

//    public Resource loadAsResource(String id) {
//        try {
//            String localStoragePath = settingFile.getName(settingFile.LOCALSTORAGEPATH);
//            Path file = Paths.get(localStoragePath).resolve(id);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                return null;
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    public Resource loadAsResourceBak(String id) {
//        try {
//            String localStoragePath = settingFile.getName(settingFile.LOCALSTORAGEPATH_BAK);
//            Path file = Paths.get(localStoragePath).resolve(id);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                return null;
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public Resource loadAsResourcImg(String id) {
//        try {
//            String localStoragePath =  settingFile.getName(settingFile.LOCALSTORAGEPATH_GOOGLE_IMG);
//            Path file = Paths.get(localStoragePath).resolve(id);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                return null;
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
