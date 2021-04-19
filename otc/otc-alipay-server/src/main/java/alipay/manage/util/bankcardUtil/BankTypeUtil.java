package alipay.manage.util.bankcardUtil;


import cn.hutool.core.util.StrUtil;

/**
 * 不同银行卡的  细节区分   比如 回调标记   缓存标记区分    数据保存 修改  等等
 */
public class BankTypeUtil {


    private static final String BANK_NAME_CCB = "建设银行";
    private static final String BANK_NAME_PSBC = "邮储银行";
    private static final String BANK_NAME_ICBC = "工商银行";
    private static final String BANK_NAME_ABC = "农业银行";
    private static final String BANK_NAME_CMB = "招商银行";


    /**
     * 保存银行卡时，截取保存的重要信息， 这里的重要信息包括 银行回调标记
     *
     * @param bankName    银行名        建设银行    工商银行  等
     * @param bankAccount 银行账号
     */
    public static String replaceBank(String bankName, String bankAccount) {
        bankName = bankName.trim();
        int length = bankAccount.length();
        switch (bankName) {
            case BANK_NAME_CCB:
            case BANK_NAME_ICBC:
            case BANK_NAME_ABC:
            case BANK_NAME_CMB:
                return StrUtil.sub(bankAccount, 0, length - 4);
            case BANK_NAME_PSBC:
                return StrUtil.sub(bankAccount, 0, length - 3);
        }
        return null;
    }


}
