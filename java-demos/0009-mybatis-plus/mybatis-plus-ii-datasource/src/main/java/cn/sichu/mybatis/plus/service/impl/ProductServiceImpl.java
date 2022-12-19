package cn.sichu.mybatis.plus.service.impl;

import cn.sichu.mybatis.plus.mapper.ProductMapper;
import cn.sichu.mybatis.plus.pojo.Product;
import cn.sichu.mybatis.plus.service.ProductService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author sichu
 * @date 2022/12/07
 **/
@DS("slave_1")
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
