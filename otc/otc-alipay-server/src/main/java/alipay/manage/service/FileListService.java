package alipay.manage.service;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.List;

public interface FileListService {
    /**
     * <p>图片数据保存</p>
     * @param editGatheringCode
     * @return
     */
    boolean addQr(FileList editGatheringCode);
    /**
     * <p>分页查询二维码数据</p>
     * @param qr
     * @return
     */
    List<FileList> findQrPage(FileList qr);
    /**
     * <p>更新二维码信息</p>
     * @param editGatheringCode
     * @return
     */
    boolean updataQr(FileList editGatheringCode);

    /**
     * <p>删除一个二维码，根据二维码id</p>
     * @param id
     * @return
     */
    boolean deleteQrById(String id);

    /**
     * <p>修改二维码为不可使用,根据二维码id</p>
     * @param is
     * @return
     */
    boolean updataStatusSu(String is);

    /**
     * <p>修改二维码为可用，根据二维码id</p>
     * @param is
     * @return
     */
    boolean updataQrStatusEr(String is);

    boolean isClickQrcodePhone(String qrcodePhone, String userId);

    /**
     * <p>获取当前未剪裁的图片</p>
     * @return
     */
    List<FileList> findQrcodeAll();

    /**
     * <p>裁剪二维码标记</p>
     * @param qrcodeId
     */
    boolean  updataQrCut(String qrcodeId);


    /**
     * <p>根据二维码编号查询二维码</p>
     * @param orderQr
     * @return
     */
    FileList findQrByNo(String orderQr);

    /**
     * <p>查询二维码，分页</p>
     * @param qrcode
     * @return
     */
    List<FileList> findIsMyQrcodePage(FileList qrcode);

    /**
     *	<p>将支付宝账户置为不可用</p>
     * @param id
     * @return
     */
    boolean updataQrStatusErByAlipayAccount(String id);

    /**
     * <p>将支付宝账户置为可用</p>
     * @param id
     * @return
     */
    boolean updataQrStatusSuByAlipayAccount(String id);

    /**
     * <p>根据支付宝账户查询</p>
     * @param qrcodeNumber		支付宝账户
     * @param accountId
     * @return
     */
    FileList findQrByNumber(String qrcodeNumber, String accountId);

    /**
     * <p>修改时间</p>
     * @param i
     */
    void updataAmount(int i);

    FileList findQrByAlipayandUser(String accountId, String alipayAccount, BigDecimal amount);


    public List<FileList> findQrByAccontId(String accountId);

    public List<FileList> findAllQr();

    /**
     * <p>根据id查询二维码账户</p>
     * @param id
     * @return
     */
    FileList findQrId(String id);

    /**
     * <p>根据金额渠道所有符合要求的二维码</p>
     * @param amount
     * @return
     */
    List<FileList> findQrByAmount(BigDecimal amount);

    /**
     *	<p>逻辑删除一张图片</p>
     * @param imgName
     * @return
     */
    boolean updataIsDealEr(String imgName);

    /**
     * <p>查询存在今天和昨天存在问题的二维码图片</p>
     * @return
     */
    List<FileList> findQrEr();

    /**
     * <p>根据收款媒介Id查询该媒介下的所有二维码</p>
     * @param mediumId
     * @return
     */
    List<FileList> findQrByMediumId(String mediumId);

    /**
     * <p>根据二维码编号，金额，媒介编号，生成一条二维码数据</p>
     * @param qrcodeId
     * @param mediumId
     * @param amount
     * @return
     */
    Result addQrByMedium(String qrcodeId, String mediumId, String amount, String userId, String flag);

    /**
     * <p>删除一个二维码，根据二维码本地编号</p>
     * @param qrcodeId
     * @return
     */
    Boolean deleteQrByQrcodeId(String qrcodeId);
    List<Medium> findIsDealMedium(String mediumAlipay);
    /**
     * <p>根据收款媒介删除二维码</p>
     * @param mediumId
     * @return
     */
    Boolean deleteQrByMediumId(String mediumId);

    /**
     * <p>用于数据更新</p>
     * @param qrcodeholder
     * @param qrcodeNumber
     * @param mediumId
     */
    void updataConcealId(String qrcodeholder, String qrcodeNumber, String mediumId);

    /**
     * 查询上传二维码金额
     * @param concealId
     * @return
     */
    List<String> findQrAmountList(String concealId);

    
    /**
     * <p>获取未剪裁的图片</p>
     * @return
     */
	List<FileList> findFileNotCut();

	/**
	 * <p>删除二维码【远程调用】</p>
	 * @param fileId
	 */
	void deleteFile(String fileId);

	/**
	 * <p>将二维码标记为已剪裁</p>
	 * @param fileId
	 */
	void updataFileIsCut(String fileId);



}