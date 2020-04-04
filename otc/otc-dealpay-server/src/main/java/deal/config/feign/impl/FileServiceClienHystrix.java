package deal.config.feign.impl;

import org.springframework.stereotype.Component;

import deal.config.feign.FileServiceClien;
import otc.api.impl.FileServiceClienFeignHystrix;
@Component
public class FileServiceClienHystrix extends FileServiceClienFeignHystrix implements FileServiceClien{

}
