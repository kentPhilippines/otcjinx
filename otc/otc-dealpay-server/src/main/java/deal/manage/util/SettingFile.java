package deal.manage.util;

import org.springframework.stereotype.Component;

import cn.hutool.setting.Setting;

/**
 * <p>外部配置文件获取类</p>
 * @author K
 */
@Component
public class SettingFile {
	public static final String FREEZE_AMOUNT_VIRTUAL = "FREEZE_AMOUNT_VIRTUAL";//虚拟金额分界线,该分界线为  大额金额分界线
	public static final String FREEZE_SECOND_VIRTUAL = "FREEZE_SECOND_VIRTUAL";//虚拟冻结大额金额时间 单位：秒
	public static final String FREEZE_PLAIN_VIRTUAL = "FREEZE_PLAIN_VIRTUAL";//虚拟冻结普通金额时间 单位：秒
	
	
	private Setting setting = new Setting();
	public Setting getSetting() {
		return setting;
	}
	public void setSetting(Setting setting) {
		this.setting = setting;
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

}
