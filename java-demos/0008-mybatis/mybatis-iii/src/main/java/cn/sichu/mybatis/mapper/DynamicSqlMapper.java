package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/04
 **/
public interface DynamicSqlMapper {
    /**
     * 多条件查询
     */
    List<Emp> getEmpByConditions(Emp emp);

    /**
     * 测试 choose, when, otherwise
     */
    List<Emp> getEmpByChoose(Emp emp);

    /**
     * 通过数组实现批量删除
     */
    int deleteMultiByArray(@Param("eids") Integer[] eids);

    /**
     * 通过List集合实现批量添加
     */
    int insertMultiByList(@Param("emps") List<Emp> emps);
}
