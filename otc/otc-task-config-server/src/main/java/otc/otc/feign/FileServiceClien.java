package otc.otc.feign;

import org.springframework.cloud.openfeign.FeignClient;

import otc.api.FileServiceClienFeign;
import otc.common.PayApiConstant;
import otc.otc.feign.impl.FileServiceClienHystrix;
@FeignClient(value=PayApiConstant.Server.FILE_SERVER, fallback = FileServiceClienHystrix.class )
public interface FileServiceClien extends FileServiceClienFeign {

}
