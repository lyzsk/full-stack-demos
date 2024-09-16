package cn.sichu.service;

import cn.sichu.entity.Eqp;

import java.text.ParseException;
import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
public interface IEqpService {

    /**
     * @param cCount CMP count
     * @param yCount YE count
     * @param qCount OQA count
     * @return int
     * @author sichu huang
     * @date 2024/09/16
     **/
    int initTable(int cCount, int yCount, int qCount) throws ParseException;

    /**
     * @param gender     gender
     * @param maxAge     maxAge
     * @param keySkills  keySkills
     * @param skillCount skillCount
     * @return int
     * @author sichu huang
     * @date 2024/09/16
     **/
    int updateOwner(String gender, int maxAge, List<String> keySkills, int skillCount);

    /**
     * @param status status
     * @return java.util.List<cn.sichu.entity.Eqp>
     * @author sichu huang
     * @date 2024/09/16
     **/
    List<Eqp> selectEqpByStatus(String status);

    int updateEqpByStatus(String status);
}
