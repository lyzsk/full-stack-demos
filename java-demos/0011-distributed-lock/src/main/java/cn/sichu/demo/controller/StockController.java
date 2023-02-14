package cn.sichu.demo.controller;

import cn.sichu.demo.model.Stock;
import cn.sichu.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sichu
 * @date 2023/02/14
 **/
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/stock/deduct")
    public String deduct() {
        stockService.deduct();
        return "success!";
    }
}
