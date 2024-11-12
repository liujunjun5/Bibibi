package com.Bibibi.enums;

public enum VideoStatusEnum {
    STATUS0(0, "转码中"),
    STATUS1(1, "转码失败"),
    STATUS2(2, "待审核"),
    STATUS3(3, "审核成功"),
    STATUS4(4, "审核失败");

    private Integer status;
    private String desc;

    VideoStatusEnum(Integer status, String desc) {
        this.desc = desc;
        this.status = status;
    }

    public Integer getStatus() {return status;}

    public String getDesc() {return desc;}

    public static VideoStatusEnum getByStatus(Integer status) {
        for (VideoStatusEnum statusEnum : VideoStatusEnum.values()) {
            if (statusEnum.getStatus().equals(status)) {
                return statusEnum;
            }
        }
        return null;
    }
}
