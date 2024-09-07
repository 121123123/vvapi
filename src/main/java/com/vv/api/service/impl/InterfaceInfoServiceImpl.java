package com.vv.api.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.api.common.ErrorCode;
import com.vv.api.exception.BusinessException;
import com.vv.api.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.vv.api.model.entity.InterfaceInfo;
import com.vv.api.model.vo.InterfaceInfoVO;
import com.vv.api.service.InterfaceInfoService;
import com.vv.api.mapper.InterfaceInfoMapper;
import com.vv.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zx
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-09-05 22:05:47
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    /**
     * 对于增加和修改，校验对应的参数
     * @param interfaceInfo 参数信息
     * @param add   是否为新建的接口
     */
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();

        String method = interfaceInfo.getMethod();

        if (add) {
            if (StringUtils.isBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称不能为空");
            }
            if (ObjectUtil.isEmpty(method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求方法不能为空");
            }
        }
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        Integer status = interfaceInfo.getStatus();

        if (StringUtils.isNotBlank(description) && description.length() > 256) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口描述不能超过256个字符");
        }
        if (StringUtils.isNotBlank(url) && url.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口地址不能超过512个字符");
        }
        if (StringUtils.isNotBlank(requestHeader) && requestHeader.length() > 2048) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求头不能超过2048个字符");
        }
        if (StringUtils.isNotBlank(responseHeader) && responseHeader.length() > 2048) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "响应头不能超过2048个字符");
        }

        if (ObjectUtil.isNotEmpty(status) && (status != 0 && status != 1)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态值不合法");
        }
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        return InterfaceInfoVO.objToVo(interfaceInfo);
    }

    /**
     * 根据前端信息返回一个 queryWrapper 对象
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return interfaceInfoQueryWrapper;
        }
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String searchText = interfaceInfoQueryRequest.getSearchText();

        interfaceInfoQueryWrapper.eq(ObjectUtil.isNotNull(id), "id", id);
        interfaceInfoQueryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
        interfaceInfoQueryWrapper.eq(StringUtils.isNotBlank(description), "description", description);
        interfaceInfoQueryWrapper.like(StringUtils.isNotBlank(url), "url", url);
        interfaceInfoQueryWrapper.like(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
        interfaceInfoQueryWrapper.like(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
        interfaceInfoQueryWrapper.eq(ObjectUtil.isNotNull(status), "status", status);
        interfaceInfoQueryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
        interfaceInfoQueryWrapper.eq(ObjectUtil.isNotNull(userId), "userId", userId);
        if (StringUtils.isNotBlank(searchText)) {
            interfaceInfoQueryWrapper.and(wrapper -> wrapper.like("name", searchText)
                    .or().like("description", searchText));
        }
        return interfaceInfoQueryWrapper;
    }

    /**
     * 为用户展示的列表
     * @param interfaceInfoPage
     * @param request
     * @return
     */
    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> infoPageRecords = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (infoPageRecords.isEmpty()) {
            return interfaceInfoVOPage;
        }
        List<InterfaceInfoVO> interfaceInfoRecords = infoPageRecords.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = BeanUtil.copyProperties(interfaceInfo, InterfaceInfoVO.class);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        interfaceInfoVOPage.setRecords(interfaceInfoRecords);
        return interfaceInfoVOPage;
    }
}




