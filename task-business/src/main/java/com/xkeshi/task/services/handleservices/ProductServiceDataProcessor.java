package com.xkeshi.task.services.handleservices;


import com.xkeshi.task.handlers.AbstractDataProcessor;
import com.xkeshi.task.entities.Product;
import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.dao.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 *
 * 各个业务自定义的数据处理器  继承DataHandler
 */
@Component
public class ProductServiceDataProcessor extends AbstractDataProcessor<String,String,Product> {

    @Autowired
    private ProductDAO productDAO;

    @Override
    protected boolean transferBytesToObjectAndInsertIntoDb(String importParam,byte[] bytes) {
        String content = new String(bytes);
        String[] temp = content.split(":");
        List<Product> list = new ArrayList<>();
        for(String s : temp){
            Product product = new Product();
            product.setName(s);
            product.setSellId(Long.parseLong(importParam));
            list.add(product);
        }
        return productDAO.saveProducts(list) == list.size();
    }

    @Override
    public ServiceSupport matchServiceSupport() {
        return ServiceSupport.PRODUCT;
    }

    @Override
    protected List<Product> selectDb(String param) {
        String[] params = param.split(",");
        String name = params[0];
        String offset = params[1];
        String size = params[2];
        return productDAO.findProducts(name,Integer.parseInt(offset),Integer.parseInt(size));
    }

    @Override
    protected byte[] transferDataToByte(List<Product> list) {
        StringBuilder sb = new StringBuilder();
        for(Product p : list){
            sb.append(p.getName()).append(":");
        }
        return sb.toString().getBytes();
    }



}
