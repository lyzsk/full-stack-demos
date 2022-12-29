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

# Demo 0010 Spring Cache

用 Spring 自带的 cache 只需要由 spring-context 包就行, 无需手动导入 pom 依赖

Step1. 启动类添加 `@EnableCaching`

Step2. Controller 里注入 `CacheManager`

`org.springframework.cache.CacheManager` 底层实现是: `ConcurrentMapCacheManager`

> CacheManager 是基于内存的, 也就是每次重启服务就会清空, 这也是为啥要用 Redis

## CachePut

用法: `@CachePut(value = "cache_name", key = "cache_key")`

Example:

    ```java
        @CachePut(value = "userCache", key = "#user.id")
        @PostMapping
        public User save(User user) {
            userService.save(user);
            return user;
        }
    ```

## CacheEvict

用法: `@CacheEvict(value = "cache_name", key = "cache_key")`

Example1:

    ```java
        @CacheEvict(value = "userCache", key = "#id")
        @DeleteMapping("/{id}")
        public void delete(@PathVariable Long id) {
            userService.removeById(id);
        }
    ```

Example2:

    ```java
        @CacheEvict(value = "userCache", key = "#user.id")
        @PutMapping
        public User update(User user) {
            userService.updateById(user);
            return user;
        }
    ```

## Cacheable

用法 1: `@Cacheable(value = "cache_name", key = "cache_key")`

> 这个用法有个问题, 就是如果查的 id 不存在, 也会被缓存存起来... 相当于 {id: null} 的键值对

用法 2:

`@Cacheable(value = "cache_name", key = "cache_key"), condition = "cache_condition"`

`@Cacheable(value = "cache_name", key = "cache_key"), unless = "cache_condition")`

Example1:

    ```java
        @Cacheable(value = "userCache", key = "#id", condition = "#root.args[0] != null")
        @GetMapping("/{id}")
        public User getById(@PathVariable Long id) {
            return userService.getById(id);
        }
    ```

Example2:

    ```java
        @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
        @GetMapping("/{id}")
        public User getById(@PathVariable Long id) {
            return userService.getById(id);
        }
    ```

Example3:

    ```java
        @Cacheable(value = "userCache", key = "#user.id + '_' + #user.name")
        @GetMapping("/list")
        public List<User> list(User user) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(user.getId() != null, User::getId, user.getId())
                .eq(user.getName() != null, User::getName, user.getName());
            return userService.list(queryWrapper);
        }
    ```

# Spring Cache With Redis

Step1. 加入依赖

```pom
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
```

Step2:

添加 spring redis 相关配置 (spring.cache.redis.time-to-live 不是必需的):

```yml
spring:
    redis:
        host: localhost
        database: 0
        port: 6379
        password:
    cache:
        redis:
            time-to-live: 1800000 # 180s, 30min
```

# LICENSE

[MIT LICENSE] Copyright (c) 2022 lyzsk
