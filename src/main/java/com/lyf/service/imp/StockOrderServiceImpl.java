package com.lyf.service.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyf.common.RedisConstant;
import com.lyf.common.StockWithRedis;
import com.lyf.dao.StockOrderDao;
import com.lyf.exception.OrderException;
import com.lyf.model.Stock;
import com.lyf.model.StockOrder;
import com.lyf.service.StockOrderService;

import com.lyf.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service("stockOrderService")
public class StockOrderServiceImpl implements StockOrderService {

    @Autowired
    private StockServiceImpl stockService;

    @Autowired
    private StockOrderDao orderDao;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    private Gson gson = new GsonBuilder().create();

    @Override
    public int delOrderDBBefore() {
        return orderDao.delOrderDBBefore();
    }


    //1.0
    @Override
    public int createWrongOrder(int sid) throws Exception {
        Stock stock = checkStock(sid);  //查看库存是否>=1
        saleStock(stock);               //减库存
        int res = createOrder(stock);   //创建订单
        return res;
    }

    @Override
    public int createOptimisticOrder(int sid) throws Exception {
        // 校验库存
        Stock stock = checkStock(sid);
        // 乐观锁更新
        saleStockOptimstic(stock);
        // 创建订单
        int id = createOrder(stock);

        return id;
    }

    @Override
    public int createOrderWithLimitAndRedis(int sid) throws Exception {
        // 校验库存，从 Redis 中获取
        Stock stock = checkStockWithRedis(sid);
        // 乐观锁更新库存和Redis
        saleStockOptimsticWithRedis(stock);
        // 创建订单
        int res = createOrder(stock);

        return res;
    }

    @Override
    public void createOrderWithLimitAndRedisAndKafka(int sid) throws Exception {
        // 校验库存
        Stock stock = checkStockWithRedis(sid);
        // 下单请求发送至 kafka，需要序列化 stock
        kafkaTemplate.send(kafkaTopic, gson.toJson(stock));
        log.info("消息发送至 Kafka 成功");
    }

    @Override
    public int consumerTopicToCreateOrderWithKafka(Stock stock) throws Exception {
        // 乐观锁更新库存和 Redis
        saleStockOptimsticWithRedis(stock);
        int res = createOrder(stock);
        if (res == 1) {
            log.info("Kafka 消费 Topic 创建订单成功");
        } else {
            log.info("Kafka 消费 Topic 创建订单失败");
        }

        return res;
    }

    /**
     * Redis 校验库存
     *
     * @param sid
     */
    private Stock checkStockWithRedis(int sid){
        Integer count = Integer.parseInt(RedisUtil.get(RedisConstant.STOCK_COUNT + sid));
        Integer sale = Integer.parseInt(RedisUtil.get(RedisConstant.STOCK_SALE + sid));
        Integer version = Integer.parseInt(RedisUtil.get(RedisConstant.STOCK_VERSION + sid));
        if (count < 1) {
            log.info("库存不足");
            throw new OrderException("库存不足 Redis currentCount: " + count);
        }
        Stock stock = new Stock();
        stock.setId(sid);
        stock.setCount(count);
        stock.setSale(sale);
        stock.setVersion(version);
        // 此处应该是热更新，但是在数据库中只有一个商品，所以直接赋值
        stock.setName("手机");

        return stock;
    }

    /**
     * 更新数据库和 DB
     */
    private void saleStockOptimsticWithRedis(Stock stock) throws OrderException{
        int res = stockService.updateStockByOptimistic(stock);
        if (res == 0) {
            throw new OrderException("乐观锁版本不一致:订单跟新失败!");
        }
        // 更新 Redis
        StockWithRedis.updateStockWithRedis(stock);
    }

    /**
     * 校验库存
     */
    private Stock checkStock(int sid) throws Exception {
        Stock stock = stockService.getStockById(sid);
        if (stock.getCount() < 1) {
            throw new OrderException("库存不足");
        }
        return stock;
    }

    /**
     * 扣库存
     */
    private int saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        stock.setCount(stock.getCount() - 1);
        return stockService.updateStockById(stock);
    }

    /**
     * 乐观锁扣库存
     */
    private void saleStockOptimstic(Stock stock) throws Exception {
        int count = stockService.updateStockByOptimistic(stock);
        if (count == 0) {
            throw new OrderException("并发更新库存失败");
        }
    }

    /**
     * 创建订单
     */
    private int createOrder(Stock stock) throws Exception {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        order.setCreateTime(new Date());
        int res = orderDao.insertSelective(order);
        if (res == 0) {
            throw new OrderException("创建订单失败");
        }
        return res;
    }
}
