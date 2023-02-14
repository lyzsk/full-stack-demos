package cn.sichu.demo.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author sichu
 * @date 2023/02/14
 **/
@TableName("db_stock")
@Data
public class Stock {

    private Long id;

    private String productCode;

    private String warehouse;

    private Integer count;

    /*
    private Integer stock = 5000;
    */

}
