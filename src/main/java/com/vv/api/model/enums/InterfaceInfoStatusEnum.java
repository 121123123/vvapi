package com.vv.api.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 * @author vv先森
 * @create 2024-09-10 14:41
 */
public enum InterfaceInfoStatusEnum {
    ONLINE("上线", 1),
    OFFLINE("下线", 0);

    private final String text;

    private final int code;

    InterfaceInfoStatusEnum(String text, int code) {
        this.text = text;
        this.code = code;
    }

    //获取值列表
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.code).collect(Collectors.toList());
    }

    //根据code获取枚举值
    public static InterfaceInfoStatusEnum getByCode(int code) {
        if (ObjectUtils.isEmpty(code)) {
            return null;
        }
        for (InterfaceInfoStatusEnum anEnum : InterfaceInfoStatusEnum.values()) {
            if (anEnum.code == code) {
                return anEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public int getCode() {
        return code;
    }
}
