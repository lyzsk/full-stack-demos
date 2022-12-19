package cn.sichu.mybatis.mapper;

import cn.sichu.mybatis.pojo.Dept;
import org.apache.ibatis.annotations.Param;

/**
 * @author sichu
 * @date 2022/12/03
 **/
public interface DeptMapper {
    /**
     * 分步查询 查询员工及员工所对应的部门信息
     * step2: 通过did查询员工所对应的部门信息
     */
    Dept getEmpAndDeptByStepTwo(@Param("did") Integer did);

    /**
     * 获取部门以及部门中所有的员工信息
     */
    Dept getDeptAndEmp(@Param("did") Integer did);

    /**
     * 分步查询, 查询部门以及部门中所有的员工信息
     * step1: 查询部门信息
     */
    Dept getDeptAndEmpByStepOne(@Param("did") Integer did);
}
