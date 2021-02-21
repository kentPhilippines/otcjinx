package alipay.manage.api.channel.amount.recharge.usdt;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class USDTQrcodeUtil {
    public static final Log log = LogFactory.get();
    private static final String FORMAT_NAME = "jpeg";
    private static final String PATH = "/img/bak";

    /**
     * 将内容转换为二维码并储存到对应位置
     *
     * @param content  内容
     * @param fileName 文件名
     * @throws Exception
     */
    public static void encode(String content, String fileName) throws Exception {
        BufferedImage generate = QrCodeUtil.generate(content, QrConfig.create());
        mkdirs(PATH);
        String file = fileName + "";//生成随机文件名
        ImageIO.write(generate, FORMAT_NAME, new File(PATH + "/" + file));
        log.info("-------------------------图片生产成功-------------------------" + PATH + "/" + file);
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir。(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }


}
