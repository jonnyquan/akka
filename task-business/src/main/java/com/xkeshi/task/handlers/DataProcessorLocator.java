package com.xkeshi.task.handlers;

import com.xkeshi.task.enums.ServiceSupport;

/**
 * Created by ruancl@xkeshi.com on 2017/1/16.
 */
public interface DataProcessorLocator {

    void registHandler(AbstractDataProcessor abstractDataHandler);

    DataProcessor locationHandler(ServiceSupport serviceSupport);
}
