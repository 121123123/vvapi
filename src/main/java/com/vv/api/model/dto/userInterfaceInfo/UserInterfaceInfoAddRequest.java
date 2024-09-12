package com.vv.api.model.dto.userInterfaceInfo;


import lombok.Data;
import java.util.Date;

/**
 * @author vv先森
 * @create 2024-09-12 11:20
 */
@Data
public class UserInterfaceInfoAddRequest {

    /**
     * 调用者 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 调用总次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

}
