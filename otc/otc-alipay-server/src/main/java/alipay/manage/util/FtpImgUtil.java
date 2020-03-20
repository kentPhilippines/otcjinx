package alipay.manage.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FtpImgUtil {
    @Autowired
    SettingFile settingFile;

//    public boolean uploadFile(String originFileName, InputStream input) {
//        // 图片路径
//        boolean success = false;
//        FTPClient ftp = new FTPClient();
//        ftp.setControlEncoding("UTF-8");
//        try {
//            int reply;
//            ftp.connect(settingFile.getName(settingFile.FTP_ADDRESS),
//                    Integer.valueOf(settingFile.getName(settingFile.FTP_PORT)));// 连接FTP服务器
//            ftp.login(settingFile.getName(settingFile.FTP_USERNAME), settingFile.getName(settingFile.FTP_PASSWORD));// 登录
//            reply = ftp.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftp.disconnect();
//                return success;
//            }
//            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftp.makeDirectory(settingFile.getName(settingFile.LOCALSTORAGEPATH));
//            ftp.changeWorkingDirectory(settingFile.getName(settingFile.LOCALSTORAGEPATH));
//            ftp.storeFile(originFileName, input);
//            input.close();
//            ftp.logout();
//            success = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (ftp.isConnected()) {
//                try {
//                    ftp.disconnect();
//                } catch (IOException ioe) {
//                }
//            }
//        }
//        return success;
//    }

//    public   void downloadFtpFile(
//            String fileName) {
//        FTPClient ftpClient = null;
//        try {
//            ftpClient = getFTPClient(settingFile.getName(settingFile.FTP_ADDRESS),
//                    settingFile.getName(settingFile.FTP_USERNAME),
//                    settingFile.getName(settingFile.FTP_PASSWORD),
//                    Integer.valueOf(settingFile.getName(settingFile.FTP_PORT))
//            );
//            ftpClient.setControlEncoding("UTF-8"); // 中文支持
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.changeWorkingDirectory(settingFile.getName(settingFile.LOCALSTORAGEPATH));
//
//            File localFile = new File(settingFile.getName(settingFile.LOCALSTORAGEPATH) + File.separatorChar + fileName);
//            OutputStream os = new FileOutputStream(localFile);
//            ftpClient.retrieveFile(fileName, os);
//            os.close();
//            ftpClient.logout();
//        } catch (FileNotFoundException e) {
//            System.out.println("没有找到" + settingFile.getName(settingFile.LOCALSTORAGEPATH) + "文件");
//            e.printStackTrace();
//        } catch (SocketException e) {
//            System.out.println("连接FTP失败.");
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("文件读取错误。");
//            e.printStackTrace();
//        }
//    }

//    public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
//        FTPClient ftpClient = new FTPClient();
//        try {
//            ftpClient = new FTPClient();
//            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
//            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
//            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
//                System.out.println("未连接到FTP，用户名或密码错误。");
//                ftpClient.disconnect();
//            } else {
//                System.out.println("FTP连接成功。");
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//            System.out.println("FTP的IP地址可能错误，请正确配置。");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("FTP的端口错误,请正确配置。");
//        }
//        return ftpClient;
//    }

}
