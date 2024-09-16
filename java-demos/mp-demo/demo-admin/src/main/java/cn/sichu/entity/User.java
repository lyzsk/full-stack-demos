package cn.sichu.entity;

import cn.sichu.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@TableName("t_user")
public class User extends BaseEntity {
    private String name;
    private String gender;
    private Integer age;
    private String address;
    private String telNumber;
    private String keySkills;

    public User(String name, String gender, Integer age, String address, String telNumber, String keySkills) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.telNumber = telNumber;
        this.keySkills = keySkills;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", gender='" + gender + '\'' + ", age=" + age + ", address='"
            + address + '\'' + ", telNumber='" + telNumber + '\'' + ", keySkills='" + keySkills + '\'' + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getKeySkills() {
        return keySkills;
    }

    public void setKeySkills(String keySkills) {
        this.keySkills = keySkills;
    }

}
