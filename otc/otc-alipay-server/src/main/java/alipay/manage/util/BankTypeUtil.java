package alipay.manage.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import alipay.manage.api.channel.util.zhaunshi.BankEnum;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

public class BankTypeUtil {
	private static final Log log = LogFactory.get();
	static   Map BANK_MAP = new ConcurrentHashMap();
	
	
	static {
		initbank();
	}


	private static void initbank() {
		log.info("【初始化银行卡code】");
		BANK_MAP.put("Bankcard", "Bankcard");
		BANK_MAP.put("Alipay", "Alipay");
		BANK_MAP.put("Wechar", "Wechar");
		BANK_MAP.put("ABC", BankEnum.ABC.getBankId());
		BANK_MAP.put("ARCU", BankEnum.ARCU.getBankId());
		BANK_MAP.put("PSBC", BankEnum.PSBC.getBankId());
		BANK_MAP.put("ASCB", BankEnum.ASCB.getBankId());
		BANK_MAP.put("AYCB", BankEnum.AYCB.getBankId());
		BANK_MAP.put("BANKWF", BankEnum.BANKWF.getBankId());
		BANK_MAP.put("BGB", BankEnum.BGB.getBankId());
		BANK_MAP.put("BHB", BankEnum.BHB.getBankId());
		BANK_MAP.put("BJBANK", BankEnum.BJBANK.getBankId());
		BANK_MAP.put("BJRCB", BankEnum.BJRCB.getBankId());
		BANK_MAP.put("BOC", BankEnum.BOC.getBankId());
		BANK_MAP.put("BOCD", BankEnum.BOCD.getBankId());
		BANK_MAP.put("BOCY", BankEnum.BOCY.getBankId());
		BANK_MAP.put("BOD", BankEnum.BOD.getBankId());
		BANK_MAP.put("BOHAIB", BankEnum.BOHAIB.getBankId());
		BANK_MAP.put("BOJZ", BankEnum.BOJZ.getBankId());
		BANK_MAP.put("BOP", BankEnum.BOP.getBankId());
		BANK_MAP.put("BOQH", BankEnum.BOQH.getBankId());
		BANK_MAP.put("BOSZ", BankEnum.BOSZ.getBankId());
		BANK_MAP.put("BOYK", BankEnum.BOYK.getBankId());
		BANK_MAP.put("BOZK", BankEnum.BOZK.getBankId());
		BANK_MAP.put("BSB", BankEnum.BSB.getBankId());
		BANK_MAP.put("BZMD", BankEnum.BZMD.getBankId());
		BANK_MAP.put("CBBQS", BankEnum.CBBQS.getBankId());
		BANK_MAP.put("CBKF", BankEnum.CBKF.getBankId());
		BANK_MAP.put("CCAB", BankEnum.CCAB.getBankId());
		BANK_MAP.put("CCB", BankEnum.CCB.getBankId());
		BANK_MAP.put("CCQTGB", BankEnum.CCQTGB.getBankId());
		BANK_MAP.put("CDB", BankEnum.CDB.getBankId());
		BANK_MAP.put("CDCB", BankEnum.CDCB.getBankId());
		BANK_MAP.put("CDRCB", BankEnum.CDRCB.getBankId());
		BANK_MAP.put("CEB", BankEnum.CEB.getBankId());
		BANK_MAP.put("CGNB", BankEnum.CGNB.getBankId());
		BANK_MAP.put("CIB", BankEnum.CIB.getBankId());
		
		
		
		
		
		
		BANK_MAP.put("CITIC", BankEnum.CITIC.getBankId());
		BANK_MAP.put("CMB", BankEnum.CMB.getBankId());
		BANK_MAP.put("CMBC", BankEnum.CMBC.getBankId());
		BANK_MAP.put("COMM", BankEnum.COMM.getBankId());
		BANK_MAP.put("CQBANK", BankEnum.CQBANK.getBankId());
		BANK_MAP.put("CRCBANK", BankEnum.CRCBANK.getBankId());
		BANK_MAP.put("CSCB", BankEnum.CSCB.getBankId());
		BANK_MAP.put("CSRCB", BankEnum.CSRCB.getBankId());
		BANK_MAP.put("CZBANK", BankEnum.CZBANK.getBankId());
		BANK_MAP.put("CZCB", BankEnum.CZCB.getBankId());
		BANK_MAP.put("CZRCB", BankEnum.CZRCB.getBankId());
		BANK_MAP.put("DAQINGB", BankEnum.DAQINGB.getBankId());
		BANK_MAP.put("DLB", BankEnum.DLB.getBankId());
		BANK_MAP.put("DRCBCL", BankEnum.DRCBCL.getBankId());
		BANK_MAP.put("DYCB", BankEnum.DYCB.getBankId());
		BANK_MAP.put("DYCCB", BankEnum.DYCCB.getBankId());
		BANK_MAP.put("DZBANK", BankEnum.DZBANK.getBankId());
		BANK_MAP.put("EGBANK", BankEnum.EGBANK.getBankId());
		BANK_MAP.put("FDB", BankEnum.FDB.getBankId());
		BANK_MAP.put("FJHXBC", BankEnum.FJHXBC.getBankId());
		BANK_MAP.put("FJNX", BankEnum.FJNX.getBankId());
		BANK_MAP.put("FSCB", BankEnum.FSCB.getBankId());
		BANK_MAP.put("GCB", BankEnum.GCB.getBankId());
		BANK_MAP.put("GDB", BankEnum.GDB.getBankId());
		BANK_MAP.put("GDRCC", BankEnum.GDRCC.getBankId());
		BANK_MAP.put("GLBANK", BankEnum.GLBANK.getBankId());
		BANK_MAP.put("GRCB", BankEnum.GRCB.getBankId());
		BANK_MAP.put("GSRCU", BankEnum.GSRCU.getBankId());
		BANK_MAP.put("GXRCU", BankEnum.GXRCU.getBankId());
		BANK_MAP.put("GYCB", BankEnum.GYCB.getBankId());
		BANK_MAP.put("GZB", BankEnum.GZB.getBankId());
		BANK_MAP.put("GZRCU", BankEnum.GZRCU.getBankId());
		BANK_MAP.put("HBC", BankEnum.HBC.getBankId());
		BANK_MAP.put("HANABANK", BankEnum.HANABANK.getBankId());
		BANK_MAP.put("H3CB", BankEnum.H3CB.getBankId());
		BANK_MAP.put("HBHSBANK", BankEnum.HBHSBANK.getBankId());
		BANK_MAP.put("HBRCU", BankEnum.HBRCU.getBankId());

		
		
		BANK_MAP.put("HBYCBANK", BankEnum.HBYCBANK.getBankId());
		BANK_MAP.put("HZCCB", BankEnum.HZCCB.getBankId());
		BANK_MAP.put("HDBANK", BankEnum.HDBANK.getBankId());
		BANK_MAP.put("HKB", BankEnum.HKB.getBankId());
		BANK_MAP.put("HKBEA", BankEnum.HKBEA.getBankId());
		BANK_MAP.put("HNRCC", BankEnum.HNRCC.getBankId());
		
		
		BANK_MAP.put("HRXJB", BankEnum.HRXJB.getBankId());
		BANK_MAP.put("HZCB", BankEnum.HZCB.getBankId());
		BANK_MAP.put("ICBC", BankEnum.ICBC.getBankId());
		BANK_MAP.put("JHBANK", BankEnum.JHBANK.getBankId());
		BANK_MAP.put("JINCHB", BankEnum.JINCHB.getBankId());
		BANK_MAP.put("JLBANK", BankEnum.JLBANK.getBankId());
		BANK_MAP.put("JLRCU", BankEnum.JLRCU.getBankId());
		BANK_MAP.put("JNBANK", BankEnum.JNBANK.getBankId());
		BANK_MAP.put("JRCB", BankEnum.JRCB.getBankId());
		BANK_MAP.put("JSB", BankEnum.JSB.getBankId());
		BANK_MAP.put("JSBANK", BankEnum.JSBANK.getBankId());
		
		BANK_MAP.put("NJCB", BankEnum.NJCB.getBankId());
		BANK_MAP.put("MTBANK", BankEnum.MTBANK.getBankId());
		BANK_MAP.put("NBBANK", BankEnum.NBBANK.getBankId());
		BANK_MAP.put("NBYZ", BankEnum.NBYZ.getBankId());
		BANK_MAP.put("NCB", BankEnum.NCB.getBankId());
		BANK_MAP.put("NHB", BankEnum.NHB.getBankId());
		BANK_MAP.put("NHQS", BankEnum.NHQS.getBankId());
		BANK_MAP.put("NJCB", BankEnum.NJCB.getBankId());
		BANK_MAP.put("NXBANK", BankEnum.NXBANK.getBankId());
		BANK_MAP.put("NYNB", BankEnum.NYNB.getBankId());
		BANK_MAP.put("ORBANK", BankEnum.ORBANK.getBankId());

		
		
		BANK_MAP.put("TACCB", BankEnum.TACCB.getBankId());
		BANK_MAP.put("TCCB", BankEnum.TCCB.getBankId());
		BANK_MAP.put("TCRCB", BankEnum.TCRCB.getBankId());
		BANK_MAP.put("TZCB", BankEnum.TZCB.getBankId());
		BANK_MAP.put("URMQCCB", BankEnum.URMQCCB.getBankId());
		
		
		
		BANK_MAP.put("WHCCB", BankEnum.WHCCB.getBankId());
		BANK_MAP.put("WHRCB", BankEnum.WHRCB.getBankId());
		BANK_MAP.put("WJRCB", BankEnum.WJRCB.getBankId());
		BANK_MAP.put("WRCB", BankEnum.WRCB.getBankId());
		BANK_MAP.put("WRCB", BankEnum.WRCB.getBankId());
		BANK_MAP.put("WZCB", BankEnum.WZCB.getBankId());
		BANK_MAP.put("ZBCB", BankEnum.ZBCB.getBankId());
		
		
		
		BANK_MAP.put("ZBCB", BankEnum.ZBCB.getBankId());
		BANK_MAP.put("ZGCCB", BankEnum.ZGCCB.getBankId());
		BANK_MAP.put("ZJKCCB", BankEnum.ZJKCCB.getBankId());
		BANK_MAP.put("ZJNX", BankEnum.ZJNX.getBankId());
		BANK_MAP.put("ZJTLCB", BankEnum.ZJTLCB.getBankId());
		BANK_MAP.put("ZRCBANK", BankEnum.ZRCBANK.getBankId());
		BANK_MAP.put("ZYCBANK", BankEnum.ZYCBANK.getBankId());
		BANK_MAP.put("ZZBANK", BankEnum.ZZBANK.getBankId());
	}


	public static String getBank(String bankcode) {
		return (String) BANK_MAP.get(bankcode);
	}

	
	
	
	
	

}
