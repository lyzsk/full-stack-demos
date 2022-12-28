package cn.sichu.demo.config;

import cn.sichu.demo.utils.IdWorker;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sichu
 * @date 2022/12/27
 **/
@Configuration
@ConfigurationProperties(prefix = "utils")
public class IdWorkerConfig {
    private Long workerId;
    private Long datacenterId;

    public IdWorkerConfig() {
    }

    public IdWorkerConfig(Long workerId, Long datacenterId) {
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    @Bean
    public IdWorker createIdWorker() {
        IdWorker idWorker = new IdWorker(workerId, datacenterId);
        return idWorker;
    }

    @Override
    public String toString() {
        return "IdWorkerConfig{" + "workerId=" + workerId + ", datacenterId=" + datacenterId + '}';
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDatacenterId() {
        return datacenterId;
    }

    public void setDatacenterId(Long datacenterId) {
        this.datacenterId = datacenterId;
    }
}
