package com.vv.api.controller;

import cn.hutool.core.util.ObjectUtil;
import co.elastic.clients.ApiClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.vv.api.annotation.AuthCheck;
import com.vv.api.common.*;
import com.vv.api.constant.UserConstant;
import com.vv.api.exception.BusinessException;
import com.vv.api.exception.ThrowUtils;
import com.vv.api.model.dto.interfaceInfo.*;
import com.vv.api.model.entity.InterfaceInfo;
import com.vv.api.model.entity.User;
import com.vv.api.model.enums.InterfaceInfoStatusEnum;
import com.vv.api.model.vo.InterfaceInfoVO;
import com.vv.api.service.InterfaceInfoService;
import com.vv.api.service.UserService;
import com.vv.vvapiclientsdk.client.VvApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 *接口管理
 * @@author vv
 *  
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    
    @Resource
    private UserService userService;

    @Resource
    private VvApiClient apiClient;

    // region 增删改查

    /**
     * 创建接口
     *
     * @param addRequest 接口对应信息
     * @param request 浏览器的request，用于获取当前登录用户
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest addRequest, HttpServletRequest request) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(addRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 接口上线（仅管理员）
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (ObjectUtil.isEmpty(idRequest)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
    //     判断是否可以调用
        com.vv.vvapiclientsdk.model.User user = new com.vv.vvapiclientsdk.model.User();
        user.setName("test");
        String username = apiClient.getUserNameByPost(user);
        if (StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getCode());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 接口下线（仅管理员）
     * @param
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (ObjectUtil.isEmpty(idRequest)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 判断是否可以调用
        com.vv.vvapiclientsdk.model.User user = new com.vv.vvapiclientsdk.model.User();
        // 接口调用的细节(实现功能需要的参数)
        user.setName("test");
        String username = apiClient.getUserNameByPost(user);
        if (StringUtils.isBlank(username)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(idRequest.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getCode());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 接口调用
     * @param
     * @return
     */
    @PostMapping("/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> invokeInterfaceInfo(@RequestBody InvokeInterfaceInfoRequest invokeInterfaceInfoRequest, HttpServletRequest request) {
        if (ObjectUtil.isEmpty(invokeInterfaceInfoRequest)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(invokeInterfaceInfoRequest.getId());
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        if (interfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getCode()){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口已下线");
        }

        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        VvApiClient vvApiClient = new VvApiClient(accessKey, secretKey);
        String userRequestParams = invokeInterfaceInfoRequest.getUserRequestParams();
        Gson gson = new Gson();

        // 判断是否可以调用
        com.vv.vvapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.vv.vvapiclientsdk.model.User.class);
        String userNameByPost = vvApiClient.getUserNameByPost(user);
        return ResultUtils.success(userNameByPost);
    }

    /**
     * 根据 id 获取接口
     *
     * @param id 接口id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVO(interfaceInfo, request));
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
            HttpServletRequest request) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listMyInterfaceInfoVOByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest,
            HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        interfaceInfoQueryRequest.setUserId(loginUser.getId());
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoService.getInterfaceInfoVOPage(interfaceInfoPage, request));
    }

    /**
     * 编辑（用户）
     *
     * @param interfaceInfoEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editInterfaceInfo(@RequestBody InterfaceInfoEditRequest interfaceInfoEditRequest, HttpServletRequest request) {
        if (interfaceInfoEditRequest == null || interfaceInfoEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoEditRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User loginUser = userService.getLoginUser(request);
        long id = interfaceInfoEditRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

}
