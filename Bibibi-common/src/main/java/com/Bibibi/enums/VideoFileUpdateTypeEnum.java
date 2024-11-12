package com.Bibibi.enums;

public enum VideoFileUpdateTypeEnum {
    NO_UPDATE(0, "无更新"),
    UPDATE(1, "有更新");

    private Integer status;
    private String desc;

    VideoFileUpdateTypeEnum(Integer status, String desc) {
        this.desc = desc;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
