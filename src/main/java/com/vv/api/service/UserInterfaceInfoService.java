package com.vv.api.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.api.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.vv.api.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.vv.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.api.model.vo.InterfaceInfoVO;
import com.vv.api.model.vo.UserInterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author zx
* @description 针对表【user_interface_info】的数据库操作Service
* @createDate 2024-09-12 11:04:50
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request);

    Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    boolean invokeCount(long interfaceInfoId, long userId);
}
