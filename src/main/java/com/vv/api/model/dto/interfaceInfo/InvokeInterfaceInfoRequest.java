package com.vv.api.model.dto.interfaceInfo;


import lombok.Data;

/**
 * @author vv先森
 * @create 2024-09-05 22:43
 */
@Data
public class InvokeInterfaceInfoRequest {
    /**
     * 主键
     */
    private Long id;

    /**
     * 调用参数
     */
    private String userRequestParams;
}
