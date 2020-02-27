package alipay.manage.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;


public class MybatisGenerator {

	// 当你的项目以及部署且运行起来的时候切不可执行这个代码。这段代码的作用是逆向工程，
	//可以自动生成  entity   mapping 接口  和对应的  mapping文件  并且会自动生成一些代码。
	//  要是有人不理解的话    百度下    逆向工程
    public static void main(String[] args) throws Exception {
        String today = "2020-02-27";
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        Date now =sdf.parse(today);
        Date d = new Date();

        if(d.getTime()>now.getTime()+1000*60*60*24){
            System.err.println("――――――未成成功运行――――――");
            System.err.println("――――――未成成功运行――――――");
            System.err.println("本程序具有破坏作用，应该只运行一次，如果必须要再运行，需要修改today变量为今天，如:" + sdf.format(new Date()));
            return;
        }
        if(false)
            return;
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        InputStream is= MybatisGenerator.class.getClassLoader().getResource("generatorConfig.xml").openStream();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        is.close();
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

        System.out.println("生成代码成功，只能执行一次，以后执行会覆盖掉mapper,pojo,xml 等文件上做的修改");
    }


}
