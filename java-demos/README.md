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

# Java demos

> **_If you like this project or it helps you in some way, don't forget to star._** :star:

# demo 0011

JVM 自带 `synchronized` 或者 `ReentrantLock` 解决 **服务内** 的并发问题: 多线程访问同一共享资源(i.e stock = 5000)

测试方法: jmeter -> new thread group, 100 user, loop count 50 -> http request http 8080 get stock/deduct -> Aggregate Report

方法 1:

```java
    public synchronized void decut() {
        stock.setStock(this.stock.getStock() - 1);
        log.info("库存: {}", stock.getStock());
    }
```

方法 2:

```java
    public void deduct() {
        lock.lock();
        try {
            stock.setStock(this.stock.getStock() - 1);
            log.info("库存: {}", stock.getStock());
        } finally {
            lock.unlock();
        }
    }
```

# LICENSE

Copyright (c) 2022 lyzsk
