package com.sakura.xdvideo.controller;

import com.sakura.xdvideo.config.WechatConfig;
import com.sakura.xdvideo.domain.JsonData;
import com.sakura.xdvideo.domain.User;
import com.sakura.xdvideo.service.UserService;
import com.sakura.xdvideo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 微信相关接口
 *
 * @author sakura
 */
@Controller
@RequestMapping("/api/v1/wechat")
public class WechatController {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private UserService userService;

    /**
     * 拼装微信扫一扫登录url
     *
     * @return
     */
    @GetMapping("login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {

        /**
         * 获取开放平台重定向地址
         */
        String redirectUrl = wechatConfig.getOpenRedirectUrl();

        /**
         * 对url进行编码
         */
        String callbackUrl = URLEncoder.encode(redirectUrl, "GBK");

        String qrcodeUrl = String.format(WechatConfig.getOpenQrcodeUrl(), wechatConfig.getOpenAppid(), callbackUrl, accessPage);

        return JsonData.buildSuccess(qrcodeUrl);
    }

    /**
     * 微信扫码登录，回调地址
     *
     * @param code
     * @param state
     * @param response
     * @throws IOException
     */
    @GetMapping("/user/callback")
    public void wechatUserCallback(@RequestParam(value = "code", required = true) String code,
                                   String state, HttpServletResponse response) throws IOException {

        User user = userService.saveWeChatUser(code);
        if (user != null) {
            /**
             * 生成jwt
             */
            String token = JwtUtils.geneJsonWebToken(user);

            /**
             * state 当前用户的页面地址，需要拼接 http:// 这样才不会站内跳转
             */
            response.sendRedirect(state + "?token" + token + "&head_img=" + user.getHeadImg() + "&name" + URLEncoder.encode(user.getName(), "UTF-8"));
        }

    }
}
