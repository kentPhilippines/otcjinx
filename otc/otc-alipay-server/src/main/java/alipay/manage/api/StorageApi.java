package alipay.manage.api;

import alipay.manage.util.FtpImgUtil;
import alipay.manage.util.StorageUtil;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import otc.result.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/storage")
public class StorageApi {
    Logger log = LoggerFactory.getLogger(StorageApi.class);
    @Autowired
    StorageUtil storageUtil;
    @Autowired
    FtpImgUtil ftpImgUtil;

    @PostMapping("/uploadPic")
    @ResponseBody
    public Result uploadPic(@RequestParam("file_data") MultipartFile[] files) throws IOException {
        log.info("上传图片");
        if (ObjectUtil.isNull(files)) {
            return Result.buildFailResult("请选择要上传的图片");
        }
        List<String> storageIds = new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            String storageId = storageUtil.uploadGatheringCode(file.getInputStream(), file.getSize(),file.getContentType(), filename);
            log.info("storageId ::: " + storageId);
            storageIds.add(storageId);
        }
        return Result.buildSuccessResult(storageIds);
    }
}
