package cn.sichu.mapper;

import cn.sichu.entity.Eqp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@Mapper
public interface EqpMapper extends BaseMapper<Eqp> {
    List<Eqp> selectEqpByStatus(String status);
}
