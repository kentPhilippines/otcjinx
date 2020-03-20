package alipay.manage.service.impl;

import alipay.manage.bean.FileList;
import alipay.manage.bean.FileListExample;
import alipay.manage.bean.Medium;
import alipay.manage.mapper.FileListMapper;
import alipay.manage.service.FileListService;
import alipay.manage.service.MediumService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import otc.api.alipay.Common;
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
        return null;
    }

    @Override
    public boolean updataIsDealEr(String imgName) {
        return false;
    }

    @Override
    public List<FileList> findQrEr() {
        return null;
    }

    @Override
    public List<FileList> findQrByMediumId(String mediumId) {
        return null;
    }

    @Override
    public Result addQrByMedium(String qrcodeId, String mediumId, String amount, String userId, String flag) {
        return null;
    }

    @Override
    public Boolean deleteQrByQrcodeId(String qrcodeId) {
        return null;
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

    @Override
    public void updataConcealId(String qrcodeholder, String qrcodeNumber, String mediumId) {

    }

    @Override
    public List<String> findQrAmountList(String concealId) {
        return null;
    }
}
