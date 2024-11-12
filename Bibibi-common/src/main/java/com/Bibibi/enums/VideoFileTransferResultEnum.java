package com.Bibibi.enums;

public enum VideoFileTransferResultEnum {
    TRANSFER(0, "转码中"),
    SUCCESS(1, "转码成功"),
    FAIL(2, "转码失败");

    private Integer status;
    private String desc;

    VideoFileTransferResultEnum(Integer status, String desc) {
        this.desc = desc;
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }
}
