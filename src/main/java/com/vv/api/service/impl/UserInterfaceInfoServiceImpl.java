package com.vv.api.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.common.ErrorCode;
import com.vv.api.exception.BusinessException;
import com.vv.api.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.vv.api.model.entity.InterfaceInfo;
import com.vv.api.model.entity.User;
import com.vv.api.model.entity.UserInterfaceInfo;
import com.vv.api.model.vo.UserInterfaceInfoVO;
import com.vv.api.service.InterfaceInfoService;
import com.vv.api.service.UserInterfaceInfoService;
import com.vv.api.mapper.UserInterfaceInfoMapper;
import com.vv.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zx
 * @description 针对表【user_interface_info】的数据库操作Service实现
 * @createDate 2024-09-12 11:04:50
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 校验参数
     *
     * @param userInterfaceInfo 待校验的值
     * @param add               是否为新增(或为修改)
     */
    @Override
    public void validInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();

        // 校验参数
        if (add) {
            Long userId = userInterfaceInfo.getUserId();
            Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
            User user = userService.getById(userId);
            if (user == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
            }
            InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
            if (interfaceInfo == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在");
            }
            if (totalNum != 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (leftNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数必须大于0");
        }
    }

    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request) {
        return UserInterfaceInfoVO.objToVO(userInterfaceInfo);
    }

    @Override
    public Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper<>();

        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer status = userInterfaceInfoQueryRequest.getStatus();

        userInterfaceInfoQueryWrapper.eq(ObjectUtil.isNotEmpty(userId), "userId", userId);
        userInterfaceInfoQueryWrapper.eq(ObjectUtil.isNotEmpty(interfaceInfoId), "interfaceInfoId", interfaceInfoId);
        userInterfaceInfoQueryWrapper.eq(ObjectUtil.isNotEmpty(status), "status", status);

        return userInterfaceInfoQueryWrapper;
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId).eq("userId", userId);
        return update(updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1"));
    }
}




