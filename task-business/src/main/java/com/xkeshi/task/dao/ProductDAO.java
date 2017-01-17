package com.xkeshi.task.dao;

import com.xkeshi.task.entities.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 */
public interface ProductDAO {

    int saveProducts(@Param("productList") List<Product> productList);

    List<Product> findProducts(@Param("sellId") String sellId, @Param("offset")int offset, @Param("size")int size);
}
