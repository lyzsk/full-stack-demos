package cn.sichu.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author sichu
 * @date 2022/12/27
 **/
@Data
public class UserInfo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 创建时间
     */
    private Date createTime;

    // private LocalDateTime createTime;

    /**
     * 使用 JsonFormat 局部格式化
     */
    // @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "UTC")
    // private Date createTime;

    /**
     * 使用 SimpleDateFormat 格式化
     */
    // @JsonIgnore
    // private Date createTime;

    /**
     * 使用 DateTimeFormatter 格式化
     */
    // @JsonIgnore
    // private LocalDateTime createTime;

    /**
     * 使用 SimpleDateFormat, DateTimeFormatter 格式化
     */
    private String cTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    // private LocalDateTime updateTime;

    /**
     * 使用 SimpleDateFormat 格式化
     */
    // @JsonIgnore
    // private Date updateTime;

    /**
     * 使用 DateTimeFormatter 格式化
     */
    // @JsonIgnore
    // private LocalDateTime updateTime;

    /**
     * 使用 SimpleDateFormat, DateTimeFormatter 格式化
     */
    private String uTime;
}
