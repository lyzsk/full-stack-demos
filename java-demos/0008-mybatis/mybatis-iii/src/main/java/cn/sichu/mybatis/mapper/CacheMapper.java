package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

/**
 * @author sichu
 * @date 2022/12/04
 **/
public interface CacheMapper {
    /**
     *
     */
    Emp getEmpByEid(@Param("eid") Integer eid);

    void insertEmp(Emp emp);
}
