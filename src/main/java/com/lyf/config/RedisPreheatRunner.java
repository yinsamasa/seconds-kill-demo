package com.lyf.config;

import com.lyf.common.RedisConstant;
import com.lyf.model.Stock;
import com.lyf.service.StockService;
import com.lyf.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 *  Redis 秒杀预热
 */

public class RedisPreheatRunner implements ApplicationRunner {
    @Autowired
    private StockService stockService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 从数据库中查询热卖商品
        Stock stock = stockService.getStockById(1);

        // 删除旧缓存
        RedisUtil.del(RedisConstant.STOCK_COUNT + stock.getCount());
        RedisUtil.del(RedisConstant.STOCK_SALE + stock.getSale());
        RedisUtil.del(RedisConstant.STOCK_VERSION + stock.getVersion());


        //缓存预热
        int sid = stock.getId();
        RedisUtil.set(RedisConstant.STOCK_COUNT + sid, String.valueOf(stock.getCount()));
        RedisUtil.set(RedisConstant.STOCK_SALE + sid, String.valueOf(stock.getSale()));
        RedisUtil.set(RedisConstant.STOCK_VERSION + sid, String.valueOf(stock.getVersion()));
    }
}
