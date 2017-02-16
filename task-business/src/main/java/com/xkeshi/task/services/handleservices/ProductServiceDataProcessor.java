package com.xkeshi.task.services.handleservices;


import com.xkeshi.task.dtos.ProductExportParamDTO;
import com.xkeshi.task.dtos.ProductImportParamDTO;
import com.xkeshi.task.handlers.AbstractDataProcessor;
import com.xkeshi.task.entities.Product;
import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.dao.ProductDAO;
import java.nio.charset.StandardCharsets;
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
public class ProductServiceDataProcessor extends AbstractDataProcessor<ProductImportParamDTO,ProductExportParamDTO,Product> {

    @Autowired
    private ProductDAO productDAO;

    @Override
    protected boolean transferBytesToObjectAndInsertIntoDb(ProductImportParamDTO importParam,byte[] bytes) {
        String content = new String(bytes,StandardCharsets.UTF_8);
        String[] temp = content.split(":");
        List<Product> list = new ArrayList<>();
        for(String s : temp){
            Product product = new Product();
            product.setName(s);
            product.setSellId(importParam.getSellId());
            list.add(product);
        }
        return productDAO.saveProducts(list) == list.size();
    }

    @Override
    public ServiceSupport matchServiceSupport() {
        return ServiceSupport.PRODUCT;
    }

    @Override
    protected List<Product> selectDb(ProductExportParamDTO param,int offset,int rouCount) {
        Long sellId = param.getSellId();
        return productDAO.findProducts(sellId.toString(),offset,rouCount);
    }

    @Override
    protected byte[] transferDataToByte(List<Product> list) {
        StringBuilder sb = new StringBuilder();
        for(Product p : list){
            sb.append(p.getName()).append(':');
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }



}
