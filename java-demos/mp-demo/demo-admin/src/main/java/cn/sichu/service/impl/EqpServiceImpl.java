package cn.sichu.service.impl;

import cn.sichu.constant.EqpStatus;
import cn.sichu.constant.KeySkills;
import cn.sichu.constant.LogicDelete;
import cn.sichu.constant.Result;
import cn.sichu.entity.Eqp;
import cn.sichu.entity.User;
import cn.sichu.mapper.EqpMapper;
import cn.sichu.mapper.UserMapper;
import cn.sichu.service.IEqpService;
import cn.sichu.utils.DateUtil;
import cn.sichu.utils.RandomStringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@Service
public class EqpServiceImpl implements IEqpService {
    @Autowired
    EqpMapper eqpMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public int initTable(int cCount, int yCount, int qCount) throws ParseException {
        int insert = 0;
        int count = cCount + yCount + qCount;
        for (int i = 0; i < cCount; i++) {
            Eqp cEqp = new Eqp();
            List<String> clist = RandomStringUtil.generateRandomStringList("SC", 8, cCount);
            clist.forEach(cEqp::setEqpName);
            if (new Date().getTime() % 2 == 0) {
                cEqp.setStatus(EqpStatus.OFFLINE);
            } else {
                cEqp.setStatus(EqpStatus.ONLINE);
            }
            if (new Date().getTime() % 3 == 0) {
                cEqp.setStatus(EqpStatus.ALARM);
            }
            cEqp.setCreateBy("admin");
            cEqp.setCreateTime(DateUtil.parseToMills(new Date()));
            cEqp.setDeleted(LogicDelete.NOT_DELETED);
            insert += eqpMapper.insert(cEqp);
        }
        for (int i = 0; i < yCount; i++) {
            Eqp yEqp = new Eqp();
            List<String> ylist = RandomStringUtil.generateRandomStringList("SY", 8, yCount);
            ylist.forEach(yEqp::setEqpName);
            if (new Date().getTime() % 2 == 0) {
                yEqp.setStatus(EqpStatus.ONLINE);
            } else {
                yEqp.setStatus(EqpStatus.OFFLINE);
            }
            if (new Date().getTime() % 3 == 0) {
                yEqp.setStatus(EqpStatus.ALARM);
            }
            yEqp.setCreateBy("admin");
            yEqp.setCreateTime(DateUtil.parseToMills(new Date()));
            yEqp.setDeleted(LogicDelete.NOT_DELETED);
            insert += eqpMapper.insert(yEqp);
        }
        for (int i = 0; i < qCount; i++) {
            Eqp qEqp = new Eqp();
            List<String> qlist = RandomStringUtil.generateRandomStringList("SQ", 8, qCount);
            qlist.forEach(qEqp::setEqpName);
            qEqp.setStatus(EqpStatus.ONLINE);
            qEqp.setCreateBy("admin");
            qEqp.setCreateTime(DateUtil.parseToMills(new Date()));
            qEqp.setDeleted(LogicDelete.NOT_DELETED);
            insert += eqpMapper.insert(qEqp);
        }
        return insert == count ? Result.SUCCESS : Result.FAILED;
    }

    @Override
    public int updateOwner(String gender, int maxAge, List<String> keySkills, int skillCount) {
        int updateCount = 0;
        LambdaQueryWrapper<User> userLqw =
            new LambdaQueryWrapper<User>().eq(User::getGender, gender).le(User::getAge, maxAge)
                .orderByDesc(User::getAge);
        if (keySkills != null && !keySkills.isEmpty()) {
            StringBuilder condition = new StringBuilder();
            for (String keySkill : keySkills) {
                if (condition.length() > 0) {
                    condition.append(" OR ");
                }
                condition.append("FIND_IN_SET('").append(keySkill).append("', key_skills) > 0");
            }
            userLqw.apply("(" + condition + ")");
        }
        userLqw.having("LENGTH(key_skills) - LENGTH(REPLACE(key_skills, ',', '')) + 1 = {0}", skillCount);
        List<User> users = userMapper.selectList(userLqw);
        List<String> ownerNames = new ArrayList<>();
        List<Long> ownerIds = new ArrayList<>();
        for (User user : users) {
            ownerNames.add(user.getName());
            ownerIds.add(user.getId());
        }
        System.err.println(users.size());
        LambdaQueryWrapper<Eqp> eqpLqw =
            new LambdaQueryWrapper<Eqp>().isNull(Eqp::getOwnerId).isNull(Eqp::getOwnerName);
        List<Eqp> eqps = eqpMapper.selectList(eqpLqw);
        int min = Math.min(ownerIds.size(), ownerNames.size());
        for (int i = 0; i < Math.min(min, eqps.size()); i++) {
            Eqp eqp = eqps.get(i);
            String name = ownerNames.get(i);
            eqp.setOwnerId(ownerIds.get(i));
            eqp.setOwnerName(name);
            eqp.setUpdateBy(name);
            eqp.setUpdateTime(new Date());
            updateCount += eqpMapper.updateById(eqp);
        }
        return updateCount;
    }

    @Override
    public List<Eqp> selectEqpByStatus(String status) {
        return eqpMapper.selectEqpByStatus(status);
    }

    @Override
    public int updateEqpByStatus(String status) {
        int count = 0;
        List<Eqp> eqps = eqpMapper.selectEqpByStatus(status);
        List<User> users = userMapper.selectUserByKeySkills(KeySkills.PROBELM_SOLVING, KeySkills.LEADERSHIP);
        for (Eqp eqp : eqps) {
            if (users.isEmpty()) {
                continue;
            }
            for (User user : users) {
                Long id = user.getId();
                String name = user.getName();
                UpdateWrapper<Eqp> uw = new UpdateWrapper<>();
                uw.set("owner_id", id);
                uw.set("owner_name", name);
                uw.set("status", EqpStatus.ONLINE);
                uw.set("update_by", name);
                uw.set("update_time", new Date());
                uw.eq("id", eqp.getId());
                count += eqpMapper.update(null, uw);
            }
        }
        return count;
    }
}
