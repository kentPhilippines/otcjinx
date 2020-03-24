package alipay.manage.service.impl;

import alipay.manage.bean.FileListExample;
import alipay.manage.mapper.FileListMapper;
import alipay.manage.service.FileListService;
import alipay.manage.service.MediumService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import otc.api.alipay.Common;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FileListServiceImpl implements FileListService {
    static final String QR_CODE = "QR:CODE";
    @Autowired
    FileListMapper fileListMapper;
    @Autowired
    MediumService mediumServiceImpl;
    Logger log= LoggerFactory.getLogger(FileListServiceImpl.class);
    @Override
    public boolean addQr(FileList editGatheringCode) {
        return false;
    }

    @Override
    public List<FileList> queryQrcodeList() {
        return null;
    }

    @Override
    public List<FileList> queryAll(double amount) {
        return null;
    }
    /**
     * <p>分页查询</p>
     */
    @Cacheable(cacheNames= {QR_CODE},  unless="#result == null")
    @Override
    public List<FileList> findQrPage(FileList qr) {
        FileListExample example = new FileListExample();
        FileListExample.Criteria createCriteria = example.createCriteria();
        if(qr.getId() != null)
            createCriteria.andIdEqualTo(qr.getId());
        if(StrUtil.isNotBlank(qr.getCode()))
            createCriteria.andCodeEqualTo(qr.getCode());
        if(qr.getStatus() != null )
            createCriteria.andStatusEqualTo(qr.getStatus());
        if(StrUtil.isNotBlank(qr.getFileholder()));
        createCriteria.andCodeEqualTo(qr.getFileholder());
        if(StrUtil.isNotBlank(qr.getFileId()))
            createCriteria.andCodeEqualTo(qr.getFileId());
        createCriteria.andIsDealEqualTo(Common.isOk);
        List<FileList> selectByExample = fileListMapper.selectByExample(example);
        return selectByExample;
    }

    @Override
    public boolean updataQr(FileList editGatheringCode) {
        return false;
    }

    @Override
    public boolean deleteQrById(String id) {
        return false;
    }

    @Override
    public boolean updataStatusSu(String is) {
        return false;
    }

    @Override
    public boolean updataQrStatusEr(String is) {
        return false;
    }

    @Override
    public boolean isClickQrcodePhone(String qrcodePhone, String userId) {
        return false;
    }

    @Override
    public List<FileList> findQrcodeAll() {
        return null;
    }

    @Override
    public boolean updataQrCut(String qrcodeId) {
        return false;
    }

    @Override
    public List<FileList> findQrcodeAllByIsDeal() {
        return null;
    }

    @Override
    public FileList findQrByNo(String orderQr) {
        return null;
    }

    @Override
    public List<FileList> findIsMyQrcodePage(FileList qrcode) {
        return null;
    }

    @Override
    public boolean updataQrStatusErByAlipayAccount(String id) {
        return false;
    }

    @Override
    public boolean updataQrStatusSuByAlipayAccount(String id) {
        return false;
    }

    @Override
    public FileList findQrByNumber(String qrcodeNumber, String accountId) {
        return null;
    }

    @Override
    public void updataAmount(int i) {

    }
    @Override
    public FileList findQrByAlipayandUser(String accountId, String alipayAccount, BigDecimal amount) {
        return null;
    }

    @Override
    public List<FileList> findQrByAccontId(String accountId) {
        return null;
    }

    @Override
    public List<FileList> findAllQr() {
        return null;
    }

    @Override
    public FileList findQrId(String id) {
        return null;
    }

    @Override
    public List<FileList> findQrByAmount(BigDecimal amount) {
        return fileListMapper.findQrByAmount(amount);
    }

    @Override
    public boolean updataIsDealEr(String imgName) {
        return false;
    }

    @Override
    public List<FileList> findQrEr() {
        return null;
    }
    @Cacheable(cacheNames= {QR_CODE},  unless="#result == null")
    @Override
    public List<FileList> findQrByMediumId(String mediumId) {
        log.info("mediumId :::->"+ mediumId);
        FileListExample example = new FileListExample();
        FileListExample.Criteria criteria = example.createCriteria();
        criteria.andConcealIdEqualTo(mediumId);
        criteria.andIsDealEqualTo("2"); //数据逻辑可用
        List<FileList> fileResult=fileListMapper.selectByExample(example);
        log.info("fileResult   :: " + fileResult);
        return fileResult;
    }
    /**
     * <p>根据二维码编号，媒介编号，交易金额生成二位码数据</p>
     */
    @CacheEvict(value=QR_CODE, allEntries=true)
    @Override
    public Result addQrByMedium(String qrcodeId, String mediumId, String amount, String userId, String flag) {
        Medium medium = mediumServiceImpl.findMediumById(mediumId);
        if(ObjectUtil.isNull(medium))
            return Result.buildFailResult("无此收款媒介");
        FileList qrcode = new FileList();
        qrcode.setConcealId(mediumId);
        qrcode.setCode(medium.getCode()+"_qr");
        qrcode.setFileholder(qrcodeId);
        if("false".equals(flag))
            qrcode.setFixationAmount(new BigDecimal(9999.0000));
        else
            qrcode.setFixationAmount(new BigDecimal(StrUtil.isBlank(amount)?"0":amount));
        qrcode.setIsFixation(StrUtil.isBlank(amount)?"1":"2");
        qrcode.setFileholder(userId);
        qrcode.setIsDeal("2"); //二维码可用
        int insertSelective = fileListMapper.insertSelective(qrcode);
        if(insertSelective > 0 && insertSelective < 2)
            return Result.buildSuccessResult();
        return Result.buildFail();
    }

    @CacheEvict(value=QR_CODE, allEntries=true)
    @Override
    public Boolean deleteQrByQrcodeId(String qrcodeId) {
        FileListExample example = new FileListExample();
        FileListExample.Criteria criteria = example.createCriteria();
        FileList bean = new FileList();
        bean.setCreateTime(null);
        bean.setIsDeal(Common.notOk);
        bean.setStatus(Common.STATUS_IS_NOT_OK);
        criteria.andConcealIdEqualTo(qrcodeId);
        int updateByExampleSelective = fileListMapper.updateByExampleSelective(bean, example);
        return updateByExampleSelective > 0 && updateByExampleSelective < 2;
    }

    @Cacheable(cacheNames= {QR_CODE},  unless="#result == null")
    @Override
    public List<Medium> findIsDealMedium(String code) {
        List<Medium> mediumList = mediumServiceImpl.findIsDealMedium(code);
        return mediumList;
    }

    @CacheEvict(value=QR_CODE, allEntries=true)
    @Override
    public Boolean deleteQrByMediumId(String mediumId) {
        FileList qr = fileListMapper.findConcealId(mediumId);
        if(ObjectUtil.isNull(qr))
            return true;
        FileListExample example = new FileListExample();
        FileListExample.Criteria criteria = example.createCriteria();
        criteria.andConcealIdEqualTo(mediumId);
        FileList bean = new FileList();
        bean.setCreateTime(null);
        bean.setIsDeal(Common.notOk);
        bean.setStatus(Common.STATUS_IS_NOT_OK);
        int updateByExampleSelective = fileListMapper.updateByExampleSelective(bean, example);
        return updateByExampleSelective > 0 && updateByExampleSelective < 2;
    }
    @CacheEvict(value=QR_CODE, allEntries=true)
    @Override
    public void updataConcealId(String qrcodeholder, String qrcodeNumber, String mediumId) {
        FileListExample example = new FileListExample();
        FileListExample.Criteria criteria = example.createCriteria();
        FileList bean = new FileList();
        criteria.andConcealIdEqualTo(qrcodeNumber);
        criteria.andConcealIdEqualTo(qrcodeholder);
        criteria.andIsDealEqualTo( "2"); //二维码可用
        bean.setConcealId(mediumId);
        bean.setCreateTime(null);
        bean.setSubmitTime(null);
        fileListMapper.updateByExampleSelective(bean, example);
    }

    @Cacheable(cacheNames= {QR_CODE},  unless="#result == null")
    @Override
    public List<String> findQrAmountList(String concealId) {
        return fileListMapper.selectQrAmountList(concealId);
    }
}
