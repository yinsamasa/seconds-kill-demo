package com.lyf.model;

import lombok.Data;

import java.util.Date;

/**
 *   库存订单
 */

@Data
public class StockOrder {

    private Integer id;

    private Integer sid;

    private String name;

    private Date createTime;
}
