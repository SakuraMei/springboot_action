package com.sakura.xdvideo.service;

import com.sakura.xdvideo.domain.User;

/**
 * 用户业务接口类
 *
 * @author sakura
 */
public interface UserService {

    User saveWeChatUser(String code);
}
