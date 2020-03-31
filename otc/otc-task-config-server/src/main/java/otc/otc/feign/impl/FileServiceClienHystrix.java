package otc.otc.feign.impl;

import org.springframework.stereotype.Component;

import otc.api.impl.FileServiceClienFeignHystrix;
import otc.otc.feign.FileServiceClien;
@Component
public class FileServiceClienHystrix extends FileServiceClienFeignHystrix implements FileServiceClien{

}
