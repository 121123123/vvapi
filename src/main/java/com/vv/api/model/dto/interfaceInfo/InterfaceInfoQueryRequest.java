package com.vv.api.model.dto.interfaceInfo;

import lombok.Data;
/**
 * @author vv先森
 * @create 2024-09-05 22:43
 */
@Data
public class InterfaceInfoQueryRequest {

    /**
     * 主键
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 当前页码
     */
    private int current;

    /**
     * 每页大小
     */
    private int pageSize;
}
