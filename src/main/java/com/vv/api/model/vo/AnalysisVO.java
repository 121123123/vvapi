package com.vv.api.model.vo;

import com.vv.vvcommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 接口调用次数
 *
 * @@author vv
 *  
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AnalysisVO extends InterfaceInfo implements Serializable {

    /**
     * 单个接口调用总次数
     */
    private int totalNum;

    private static final long serialVersionUID = 1L;
}
