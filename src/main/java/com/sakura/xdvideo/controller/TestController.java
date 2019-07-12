package com.sakura.xdvideo.controller;

import com.sakura.xdvideo.config.WechatConfig;
import com.sakura.xdvideo.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试controller
 *
 * @author sakura
 */
@RestController
public class TestController {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private VideoMapper videoMapper;

    @RequestMapping("test_config")
    public String testConfig() {
        return wechatConfig.getAppId() + ":" + wechatConfig.getAppsecret();
    }

    @RequestMapping("test_db")
    public Object testDB() {
        return videoMapper.findAll();
    }
}
