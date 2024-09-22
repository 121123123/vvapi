package com.vv.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vv.vvcommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author zx
* @description 针对表【user_interface_info】的数据库操作Mapper
* @createDate 2024-09-12 11:04:50
* @Entity com.vv.api.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    // select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
    // 查询用户接口信息表中，按照指定的limit进行筛选
    // 返回前limit条接口的信息
    List<UserInterfaceInfo> ListTopInvokeInterfaceInfo(int limit);
}




