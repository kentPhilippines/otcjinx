package alipay.manage.service;

        import alipay.manage.bean.Product;

        import java.util.List;

public interface ProductService {
    /**
     * 查询所有产品列表
     * @return
     */
    List<Product> findAllProduct();
}
