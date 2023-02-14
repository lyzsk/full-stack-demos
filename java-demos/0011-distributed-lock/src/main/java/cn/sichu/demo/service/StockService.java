package cn.sichu.demo.service;

import cn.sichu.demo.mapper.StockMapper;
import cn.sichu.demo.model.Stock;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sichu
 * @date 2023/02/14
 **/
@Service
@Slf4j
public class StockService {

    // private Stock stock = new Stock();

    @Autowired
    private StockMapper stockMapper;

    private ReentrantLock lock = new ReentrantLock();


    /**
     * 和用 ReentrantLock 效果相同
    public synchronized void decut() {
        stock.setStock(this.stock.getStock() - 1);
        log.info("库存: {}", stock.getStock());
    }
    */

    public void deduct() {
        // lock.lock();
        try {
            // stock.setStock(this.stock.getStock() - 1);
            // log.info("库存: {}", stock.getStock());

            Stock stock = this.stockMapper.selectOne(new QueryWrapper<Stock>().eq("product_code", "1001"));
            if (stock != null && stock.getCount() > 0) {
                stock.setCount(stock.getCount() - 1);
                this.stockMapper.updateById(stock);
            }
        } finally {
            // lock.unlock();
        }
    }
}
