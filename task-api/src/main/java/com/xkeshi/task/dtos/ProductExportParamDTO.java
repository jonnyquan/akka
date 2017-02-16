package com.xkeshi.task.dtos;

import java.io.Serializable;

/**
 * Created by ruancl@xkeshi.com on 2017/1/17.
 */
public class ProductExportParamDTO implements Serializable{

    private Long sellId;


    public Long getSellId() {
        return sellId;
    }

    public void setSellId(Long sellId) {
        this.sellId = sellId;
    }

}
