package com.sakura.xdvideo.service.impl;

import com.sakura.xdvideo.config.WechatConfig;
import com.sakura.xdvideo.domain.User;
import com.sakura.xdvideo.mapper.UserMapper;
import com.sakura.xdvideo.service.UserService;
import com.sakura.xdvideo.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * @author sakura
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveWeChatUser(String code) {

        String accessTokenUrl = String.format(WechatConfig.getOpenAccessTokenUrl(), wechatConfig.getOpenAppid(), wechatConfig.getOpenAppsecret(), code);

        /**
         * 获取access_token以及openid
         */
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);
        if (baseMap == null || baseMap.isEmpty()) {
            return null;
        }
        String accessToken = (String) baseMap.get("access_token");
        String openId = (String) baseMap.get("openid");

        User dbUser = userMapper.findByopenid(openId);
        /**
         * 用户已存在
         * 更新用户,或者直接返回
         */
        if (dbUser != null) {
            return dbUser;
        }

        /**
         * 获取用户基本信息
         */
        String userInfoUrl = String.format(WechatConfig.getOpenUserInfoUrl(), accessToken, openId);
        Map<String, Object> baseUserMap = HttpUtils.doGet(userInfoUrl);
        if (baseUserMap == null || baseMap.isEmpty()) {
            return null;
        }
        String nickname = (String) baseMap.get("nickname");

        Double sexTemp = (Double) baseMap.get("sex");
        int sex = sexTemp.intValue();

        String province = (String) baseMap.get("province");
        String city = (String) baseMap.get("city");
        String country = (String) baseMap.get("country");
        String headimgurl = (String) baseMap.get("headimgurl");
        StringBuilder stringBuilder = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = stringBuilder.toString();
        try {
            /**
             * 解决乱码
             */
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenid(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());

        /**
         * 用户不存在，则添加用户
         */
        userMapper.save(user);

        return user;
    }
}
