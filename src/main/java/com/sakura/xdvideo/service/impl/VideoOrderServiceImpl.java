package com.sakura.xdvideo.service.impl;

import com.sakura.xdvideo.config.WechatConfig;
import com.sakura.xdvideo.domain.User;
import com.sakura.xdvideo.domain.Video;
import com.sakura.xdvideo.domain.VideoOrder;
import com.sakura.xdvideo.dto.VideoOrderDto;
import com.sakura.xdvideo.mapper.UserMapper;
import com.sakura.xdvideo.mapper.VideoMapper;
import com.sakura.xdvideo.mapper.VideoOrderMapper;
import com.sakura.xdvideo.service.VideoOrderService;
import com.sakura.xdvideo.utils.CommonUtils;
import com.sakura.xdvideo.utils.HttpUtils;
import com.sakura.xdvideo.utils.WxPayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 订单接口实现类
 *
 * @author sakura
 */
@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WechatConfig weChatConfig;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String save(VideoOrderDto videoOrderDto) throws Exception {

        dataLogger.info("module=video_order`api=save`user_id={}`video_id={}", videoOrderDto.getUserId(), videoOrderDto.getVideoId());

        //查找视频信息
        Video video = videoMapper.findById(videoOrderDto.getVideoId());

        //查找用户信息
        User user = userMapper.findByid(videoOrderDto.getUserId());


        //生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());
        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.genetateUUID());

        videoOrderMapper.insert(videoOrder);

        //获取codeurl
        String codeUrl = unifiedOrder(videoOrder);

        return codeUrl;
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {

        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public int updateVideoOderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOderByOutTradeNo(videoOrder);
    }

    /**
     * 统一下单方法
     *
     * @return
     */
    private String unifiedOrder(VideoOrder videoOrder) throws Exception {

        //生成签名
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.genetateUUID());
        params.put("body", videoOrder.getVideoTitle());
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip", videoOrder.getIp());
        params.put("notify_url", weChatConfig.getPayCallbackUrl());
        params.put("trade_type", "NATIVE");

        //sign签名
        String sign = WxPayUtils.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);

        //map转xml
        String payXml = WxPayUtils.mapToXml(params);

        //统一下单
        String orderStr = HttpUtils.doPost(WechatConfig.getUnifiedOrderUrl(), payXml, 4000);
        if (orderStr == null) {
            return null;
        }

        Map<String, String> unifiedOrderMap = WxPayUtils.xmlToMap(orderStr);
        System.out.println(unifiedOrderMap.toString());
        if (unifiedOrderMap != null) {
            return unifiedOrderMap.get("code_url");
        }

        return null;
    }
}
