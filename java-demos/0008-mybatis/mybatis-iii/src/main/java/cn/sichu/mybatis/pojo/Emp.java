package cn.sichu.mybatis.pojo;

import java.io.Serializable;

/**
 * @author sichu
 * @date 2022/12/07
 **/
public class Emp implements Serializable {
    private Integer eid;
    private String empName;
    private Integer age;
    private String gender;
    private String email;
    private Dept dept;

    public Emp(Integer eid, String empName, Integer age, String gender, String email, Dept dept) {
        this.eid = eid;
        this.empName = empName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.dept = dept;
    }

    public Emp() {
    }

    public Emp(Integer eid, String empName, Integer age, String gender, String email) {
        this.eid = eid;
        this.empName = empName;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Emp{" + "eid=" + eid + ", empName='" + empName + '\'' + ", age=" + age + ", gender='" + gender + '\''
            + ", email='" + email + '\'' + ", dept=" + dept + '}';
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
