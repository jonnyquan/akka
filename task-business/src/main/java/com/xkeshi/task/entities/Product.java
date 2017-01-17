package com.xkeshi.task.entities;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 */
public class Product {

    private Long id;

    private String name;

    private Long sellId;

    public Long getSellId() {
        return sellId;
    }

    public void setSellId(Long sellId) {
        this.sellId = sellId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
