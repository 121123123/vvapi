package com.vv.api.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.common.ErrorCode;
import com.vv.api.exception.BusinessException;
import com.vv.api.mapper.InterfaceInfoMapper;
import com.vv.api.mapper.UserMapper;
import com.vv.api.service.UserService;
import com.vv.vvcommon.model.entity.InterfaceInfo;
import com.vv.vvcommon.model.entity.User;
import com.vv.vvcommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author vv先森
 * @create 2024-09-21 11:28
 */
@DubboService
public class InnerUserServiceImpl extends ServiceImpl<UserMapper, User>
        implements InnerUserService  {

    @Resource
    private UserService userService;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey", accessKey);
        User user = userService.getOne(userQueryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return user;
    }
}
