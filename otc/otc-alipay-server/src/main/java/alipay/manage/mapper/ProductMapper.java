package alipay.manage.mapper;

import alipay.manage.bean.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAllProduct();
}
