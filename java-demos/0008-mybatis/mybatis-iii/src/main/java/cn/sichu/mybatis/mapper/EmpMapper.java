package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/03
 **/
public interface EmpMapper {
    /**
     * 查询所有的员工信息
     */
    List<Emp> getAllEmp();

    /**
     * 查询员工 以及 员工所对应的部门信息
     */
    Emp getEmpAndDept(@Param("eid") Integer eid);

    /**
     * 分步查询 查询员工及员工所对应的部门信息
     * step1: 查询员工信息
     */
    Emp getEmpAndDeptByStepOne(@Param("eid") Integer eid);

    /**
     * 分步查询, 查询部门以及部门中所有的员工信息
     * step2: 查询部门中所有的员工信息
     */
    List<Emp> getDeptAndEmpByStepTwo(@Param("did") Integer did);
}
