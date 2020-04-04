package deal.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

import deal.config.feign.impl.FileServiceClienHystrix;
import otc.api.FileServiceClienFeign;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.FILE_SERVER, fallback = FileServiceClienHystrix.class )
public interface FileServiceClien extends FileServiceClienFeign {

}
