package com.vv.api.service;

import com.vv.api.model.entity.PostThumb;
import com.vv.api.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 帖子点赞服务
 *
 * @@author vv
 *  
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
