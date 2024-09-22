package com.vv.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.vv.api.annotation.AuthCheck;
import com.vv.api.common.BaseResponse;
import com.vv.api.common.ErrorCode;
import com.vv.api.common.ResultUtils;
import com.vv.api.exception.BusinessException;
import com.vv.api.mapper.UserInterfaceInfoMapper;
import com.vv.api.model.vo.AnalysisVO;
import com.vv.api.model.vo.InterfaceInfoVO;
import com.vv.api.service.InterfaceInfoService;
import com.vv.api.service.UserInterfaceInfoService;
import com.vv.vvcommon.model.entity.InterfaceInfo;
import com.vv.vvcommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author vv先森
 * @create 2024-09-22 9:36
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;
    @GetMapping("/top")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<AnalysisVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.ListTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoMap = userInterfaceInfos.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoMap.keySet());
        List<InterfaceInfo> interfaceInfos = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(interfaceInfos)) {
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<AnalysisVO> interfaceInfoVOList = interfaceInfos.stream().map(interfaceInfo -> {
            // 创建一个新的接口信息VO对象
            AnalysisVO analysisVO = new AnalysisVO();
            // 将接口信息复制到接口信息VO对象中
            BeanUtils.copyProperties(interfaceInfo, analysisVO);
            // 从接口信息ID对应的映射中获取调用次数
            int totalNum = interfaceInfoMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            // 将调用次数设置到接口信息VO对象中
            analysisVO.setTotalNum(totalNum);
            // 返回构建好的接口信息VO对象
            return analysisVO;
        }).collect(Collectors.toList());
        // 返回处理结果
        return ResultUtils.success(interfaceInfoVOList);
    }
}
