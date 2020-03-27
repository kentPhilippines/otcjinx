package alipay.manage.service.impl;

import alipay.manage.bean.Product;
import alipay.manage.mapper.ProductMapper;
import alipay.manage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Override
    public List<Product> findAllProduct() {
        return productMapper.findAllProduct();
    }
}
