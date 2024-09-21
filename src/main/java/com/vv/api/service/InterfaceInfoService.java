package com.vv.api.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.api.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.vo.InterfaceInfoVO;
import com.vv.vvcommon.model.entity.InterfaceInfo;

import javax.servlet.http.HttpServletRequest;

/**
* @author zx
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-09-05 22:05:47
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);
}
