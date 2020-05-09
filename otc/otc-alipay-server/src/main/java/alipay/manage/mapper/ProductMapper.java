package alipay.manage.mapper;

import alipay.manage.bean.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAllProduct();

    
    
    @Select("select * from alipay_product where `describe` =  #{bank}")
	List<Product> findCode(@Param("bank") String bank);
}
