package cn.sichu.mybatis.test;

import cn.sichu.mybatis.mapper.DeptMapper;
import cn.sichu.mybatis.mapper.EmpMapper;
import cn.sichu.mybatis.pojo.Dept;
import cn.sichu.mybatis.pojo.Emp;
import cn.sichu.mybatis.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/03
 **/
public class ResultMapTest {
    @Test
    public void testGetAllEmp() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        List<Emp> res = mapper.getAllEmp();
        res.forEach(emp -> System.out.println(emp));
    }

    @Test
    public void testGetEmpAndDept() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = mapper.getEmpAndDept(3);
        System.out.println(emp);
    }

    @Test
    public void testGetEmpAndDeptByStep() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        EmpMapper mapper = sqlSession.getMapper(EmpMapper.class);
        Emp emp = mapper.getEmpAndDeptByStepOne(2);
        // System.out.println(emp);
        System.out.println(emp.getEmpName());
        System.out.println("*************");
        System.out.println(emp.getDept());
    }

    @Test
    public void testGetDeptAndEmp() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = mapper.getDeptAndEmp(1);
        System.out.println(dept);
    }

    @Test
    public void testGetDeptAndEmpByStep() {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
        DeptMapper mapper = sqlSession.getMapper(DeptMapper.class);
        Dept dept = mapper.getDeptAndEmpByStepOne(1);
        // System.out.println(dept);
        // System.out.println(dept.getDeptName());
        System.out.println(dept.getEmps());
    }
}
