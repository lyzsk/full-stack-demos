package cn.sichu.demo.mapper;

import cn.sichu.demo.model.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sichu
 * @date 2023/02/14
 **/
@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}
