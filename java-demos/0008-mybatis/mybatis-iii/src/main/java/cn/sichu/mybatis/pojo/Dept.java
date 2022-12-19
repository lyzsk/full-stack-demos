package cn.sichu.mybatis.pojo;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/07
 **/
public class Dept {
    private Integer did;

    private String deptName;

    private List<Emp> emps;

    public Dept(Integer did, String deptName, List<Emp> emps) {
        this.did = did;
        this.deptName = deptName;
        this.emps = emps;
    }

    public Dept() {
    }

    public Dept(Integer did, String deptName) {
        this.did = did;
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "Dept{" + "did=" + did + ", deptName='" + deptName + '\'' + ", emps=" + emps + '}';
    }

    public List<Emp> getEmps() {
        return emps;
    }

    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
