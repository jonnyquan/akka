package com.xkeshi.task.enums;

/**
 * Created by ruancl@xkeshi.com on 2017/1/11.
 */
public enum PackageMethod {
    ZIP("zip");

    private String name;

    public String getName() {
        return name;
    }

    PackageMethod(String name) {
        this.name = name;
    }
}
