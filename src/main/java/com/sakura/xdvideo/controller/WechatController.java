package com.sakura.xdvideo.controller;

import com.sakura.xdvideo.config.WechatConfig;
import com.sakura.xdvideo.domain.JsonData;
import com.sakura.xdvideo.domain.User;
import com.sakura.xdvideo.domain.VideoOrder;
import com.sakura.xdvideo.service.UserService;
import com.sakura.xdvideo.service.VideoOrderService;
import com.sakura.xdvideo.utils.JwtUtils;
import com.sakura.xdvideo.utils.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

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

    @Autowired
    private VideoOrderService videoOrderService;

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

    /**
     * 微信支付回调
     *
     * @param request
     * @param response
     */
    @RequestMapping("/order/callback")
    public void orderCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {

        InputStream inputStream = request.getInputStream();

        /**
         * BufferedReader是包装设计模式，性能更高
         */
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();

        Map<String, String> callbackMap = WxPayUtils.xmlToMap(sb.toString());

        SortedMap<String, String> sortedMap = WxPayUtils.getSortedMap(callbackMap);

        /**
         * 判断签名是否正确
         */
        if (WxPayUtils.isCorrectSign(sortedMap, wechatConfig.getKey())) {
            if ("SUCCESS".equals(sortedMap.get("result_code"))) {

                String outTradeNo = sortedMap.get("out_trade_no");

                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);

                /**
                 * 更新订单状态
                 * 判断逻辑看业务场景
                 */
                if (dbVideoOrder != null && dbVideoOrder.getState() == 0) {
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);
                    int rows = videoOrderService.updateVideoOderByOutTradeNo(videoOrder);

                    /**
                     * 影响行数row==1 或者 row==0 响应微信成功或者失败
                     * 通知微信订单处理成功
                     */
                    if (rows == 1) {
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }
            }
        }

        //都处理失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");
    }
}
