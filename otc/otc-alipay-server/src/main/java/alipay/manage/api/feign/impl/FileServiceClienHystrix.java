package alipay.manage.api.feign.impl;

import org.springframework.stereotype.Component;

import alipay.manage.api.feign.FileServiceClien;
import otc.api.impl.FileServiceClienFeignHystrix;
@Component
public class FileServiceClienHystrix extends FileServiceClienFeignHystrix implements FileServiceClien{

}
