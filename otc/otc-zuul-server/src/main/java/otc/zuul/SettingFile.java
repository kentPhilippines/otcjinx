package otc.zuul;


import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.dialect.console.ConsoleLog;
import cn.hutool.setting.Setting;

/**
 *    接口  --->  统一ip配置
 *    账户  --->  统一ip配置
 *
 *    或者其他配置
 */
public class SettingFile {
    private Setting setting = new Setting();

    public Setting getSetting() {
        return setting;
    }
    public void setSetting(Setting setting) {
        this.setting = setting;
    }
    /**
     * 获取分组配置 , 配置规则   账户-接口
     * @param group   账户分组
     * @param key     ip配置，或者其他配置
     * @return
     */
    public  String getGroupIp(String group, String key ){
        String byGroup = setting.getByGroup(key,group);
        if(StrUtil.isBlank(byGroup)){
            return null;
        }
        return byGroup;
    }
    /**
     * <p>根据key值获取value</p>
     * @param key
     * @return
     */
    public String getName(String key) {
        String string = setting.get(key);
        return string;
    }
    /**
     * <p>从新加载配置文件</p>
     * @return
     */
    public boolean load() {
        boolean load = setting.load();
        return load;
    }



    public Boolean put(String group, String key , String  value ){
        String put = null;
        if(StrUtil.isBlank(group)){
            put = setting.put(key,value);
        }else {
            put = setting.put(group,key,value);
        }
        boolean load = load();
        Console.log("当前更新数据为："+put+"");

        return Boolean.TRUE;
    }
}
