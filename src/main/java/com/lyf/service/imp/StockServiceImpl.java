package com.lyf.service.imp;

import com.lyf.dao.StockDao;
import com.lyf.model.Stock;
import com.lyf.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("stockService")
public class StockServiceImpl implements StockService {


    @Autowired
    private StockDao stockDao;

    @Override
    public int getStockCount(int id) {
        Stock stock = stockDao.selectByPrimaryKey(id);
        return stock.getCount();
    }

    @Override
    public Stock getStockById(int id) {

        return stockDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateStockById(Stock stock) {

        return stockDao.updateByPrimaryKeySelective(stock);
    }

    @Override
    public int updateStockByOptimistic(Stock stock) {

        return stockDao.updateByOptimistic(stock);
    }

    @Override
    public int initDBBefore() {

        return stockDao.initDBBefore();
    }
}
