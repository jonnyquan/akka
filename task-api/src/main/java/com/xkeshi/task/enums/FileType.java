package com.xkeshi.task.enums;

/**
 * Created by ruancl@xkeshi.com on 2017/1/11.
 */
public enum FileType {

    TXT("txt"), EXCEL("excel"), JPG("jpg");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    FileType(String name) {

        this.name = name;
    }
}
