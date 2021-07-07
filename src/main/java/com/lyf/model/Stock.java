package com.lyf.model;

import lombok.Data;

/**
 *  库存
 */

@Data
public class Stock {

    private Integer id;

    private String name;

    private Integer count;

    private Integer sale;

    private Integer version;
}
