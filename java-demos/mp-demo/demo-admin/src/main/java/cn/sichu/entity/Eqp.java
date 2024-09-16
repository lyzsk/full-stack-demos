package cn.sichu.entity;

import cn.sichu.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@TableName("t_eqp")
public class Eqp extends BaseEntity {
    private String eqpName;
    private String status;
    private Long ownerId;
    private String ownerName;

    public Eqp() {
    }

    public Eqp(String eqpName, String status, Long ownerId, String ownerName) {
        this.eqpName = eqpName;
        this.status = status;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "Eqp{" + "eqpName='" + eqpName + '\'' + ", status='" + status + '\'' + ", ownerId=" + ownerId
            + ", ownerName='" + ownerName + '\'' + '}';
    }

    public String getEqpName() {
        return eqpName;
    }

    public void setEqpName(String eqpName) {
        this.eqpName = eqpName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
