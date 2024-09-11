package com.vv.api.common;

import lombok.Data;
import org.apache.ibatis.javassist.SerialVersionUID;

import java.io.Serializable;

/**
 * @author vv先森
 * @create 2024-09-10 14:25
 */
@Data
public class IdRequest implements Serializable {

    private Long id;

    private static final long SerialVersionUID = 1L;
}
