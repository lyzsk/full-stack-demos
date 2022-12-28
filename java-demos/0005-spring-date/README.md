**[简体中文](./README.CN.md) | English**

<p align="center">
    <a href="https://github.com/lyzsk/full-stack-demos/blob/master/LICENSE">
        <img src="https://img.shields.io/github/license/lyzsk/full-stack-demos.svg?style=plastic&logo=github" />
    </a>
    <a href="https://github.com/lyzsk/full-stack-demos/members">
        <img src="https://img.shields.io/github/forks/lyzsk/full-stack-demos.svg?style=plastic&logo=github" />
    </a>
    <a href="https://github.com/lyzsk/full-stack-demos/stargazers">
        <img src="https://img.shields.io/github/stars/lyzsk/full-stack-demos.svg?style=plastic&logo=github" />
    </a>
</p>

# Demo-0005 Spring date

> **_If you like this project or it helps you in some way, don't forget to star._** :star:

# MySQL to Java

|   MySQL   | Java               |
| :-------: | :----------------- |
|   date    | java.sql.Date      |
| datetime  | java.sql.Timestamp |
| timestamp | java.sql.Timestamp |
|   time    | java.sql.time      |
|   year    | java.sql.Date      |

# MyBatis

MyBatis 映射出的是 java.util.Date 类型, java.sql.Date/Timestamp/time 都是 java.sql 包下, 都是 java.util.Date 的子类

# Test Examples

数据库字段:

|         id         | username |     create_time     |     update_time     |
| :----------------: | :------: | :-----------------: | :-----------------: |
| 792654905362108416 |  admin   | 2022-12-27 23:27:36 | 2022-12-27 23:27:36 |

## java.util.Date

```java
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
```

-   output:

    ```js
        {
            "id": 792654905362108416,
            "username": "admin",
            "createTime": "2022-12-27T23:27:36.000+00:00",
            "updateTime": "2022-12-27T23:27:36.000+00:00"
        },
    ```

## java.util.LocalDateTime

```java
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
```

-   output:

    ```js
        {
            "id": 792654905362108416,
            "username": "admin",
            "createTime": "2022-12-27T23:27:36",
            "updateTime": "2022-12-27T23:27:36"
        },
    ```

## use JsonFormat annotation

```java
    /**
     * 使用 JsonFormat 局部格式化
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "UTC")
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
```

使用 `com.fasterxml.jackson.annotation.JsonFormat` 可以对注解的实体属性格式化成想要的时间格式, 时区

-   output:

    ```js
        {
            "id": 792654905362108416,
            "username": "admin",
            "createTime": "2022-12-27 11:27:36",
            "updateTime": "2022-12-27T23:27:36.000+00:00"
        },
    ```

## java.text.SimpleDateFormat + com.fasterxml.jackson.annotation.JsonIgnore

1. 用 SimpleDateFormat 把时间格式转成 string
2. 然后再实体类里用 cTime, uTime 接收 string 类型的返回值,
3. 用 `@JsonIgnore` 隐藏原来 Date 类型的字段

```java
    /**
     * 使用 SimpleDateFormat 格式化
     */
    @JsonIgnore
    private Date createTime;

    /**
     * 使用 SimpleDateFormat, DateTimeFormatter 格式化
     */
    private String cTime;

    /**
     * 使用 SimpleDateFormat 格式化
     */
    @JsonIgnore
    private Date updateTime;

    /**
     * 使用 SimpleDateFormat, DateTimeFormatter 格式化
     */
    private String uTime;
```

4. 在 controller 里创建 SimpleDateFormat 模板对象, 对原始数据用这个对象 foreach 遍历重新 format 然后输出

```java
    @GetMapping("/list-with-sdf")
    public List<UserInfo> getListWithSDF() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<UserInfo> list = userInfoService.getListWithSDF();
        list.forEach(item -> {
            item.setCTime(simpleDateFormat.format(item.getCreateTime()));
            item.setUTime(simpleDateFormat.format(item.getUpdateTime()));
        });
        return list;
    }
```

-   output:

    ```js
        {
            "id": 792654905362108416,
            "username": "admin",
            "cTime": "2022-12-27 23:27:36",
            "uTime": "2022-12-27 23:27:36"
        },
    ```

## java.time.format.DateTimeFormatter + com.fasterxml.jackson.annotation.JsonIgnore

DateTimeFormatter 格式化时间原理和 SimpleDateFormat 是一样的, 但是 DateTimeFormatter 只支持 `LocalDateTime` 属性的值, 所以在实体类要把时间相关的类型改成 LocalDateTime

```java
    /**
     * 使用 DateTimeFormatter 格式化
     */
    @JsonIgnore
    private LocalDateTime createTime;

    /**
     * 使用 SimpleDateFormat, DateTimeFormatter 格式化
     */
    private String cTime;

    /**
     * 使用 DateTimeFormatter 格式化
     */
    @JsonIgnore
    private LocalDateTime updateTime;

    /**
     * 使用 SimpleDateFormat, DateTimeFormatter 格式化
     */
    private String uTime;
```

```java
    @GetMapping("/list-with-dtf")
    public List<UserInfo> getListWithDTF() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<UserInfo> list = userInfoService.getListWithDTF();
        list.forEach(item -> {
            item.setCTime(dateTimeFormatter.format(item.getCreateTime()));
            item.setUTime(dateTimeFormatter.format(item.getUpdateTime()));
        });
        return list;
    }
```

-   output:

    ```js
        {
            "id": 792654905362108416,
            "username": "admin",
            "utime": "2022-12-27 23:27:36",
            "ctime": "2022-12-27 23:27:36"
        },
    ```

虽然输出结果和 SimpleDateFormat 一样, 但是 `DateTimeFormatter` 是线程安全的, JDK8 以上的项目, 尽量使用 DateTimeFormatter 来进行时间格式化, 同时也证明应该把实体类的对象用 LocalDateTime 创建更方便...

## global spring jackson configuration

在 `application.yml` 里直接配置全局 jackson

```yml
spring:
    jackson:
        date-format: yyyy-MM-dd HH:mm:ss
        time-zone: UTC
```

-   output:

    ```js
        {
            "id": 792654905362108416,
            "username": "admin",
            "createTime": "2022-12-27 23:27:36",
            "updateTime": "2022-12-27 23:27:36"
        },
    ```

# LICENSE

[MIT LICENSE] Copyright (c) 2022 lyzsk

[mit license]: https://github.com/lyzsk/full-stack-demos/blob/master/LICENSE
