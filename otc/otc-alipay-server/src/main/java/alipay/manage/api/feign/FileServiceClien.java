package alipay.manage.api.feign;

import org.springframework.cloud.openfeign.FeignClient;

import alipay.manage.api.feign.impl.FileServiceClienHystrix;
import otc.api.FileServiceClienFeign;
import otc.common.PayApiConstant;
@FeignClient(value=PayApiConstant.Server.FILE_SERVER, fallback = FileServiceClienHystrix.class )
public interface FileServiceClien extends FileServiceClienFeign {

}
