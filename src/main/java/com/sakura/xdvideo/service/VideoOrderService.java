package com.sakura.xdvideo.service;

import com.sakura.xdvideo.domain.VideoOrder;
import com.sakura.xdvideo.dto.VideoOrderDto;

/**
 * 订单接口
 *
 * @author sakura
 */
public interface VideoOrderService {

    /**
     * 下单接口
     *
     * @param videoOrderDto
     * @return
     */
    String save(VideoOrderDto videoOrderDto) throws Exception;

    /**
     * 根据流水号查找订单
     *
     * @param outTradeNo
     * @return
     */
    VideoOrder findByOutTradeNo(String outTradeNo);


    /**
     * 根据流水号更新订单
     *
     * @param videoOrder
     * @return
     */
    int updateVideoOderByOutTradeNo(VideoOrder videoOrder);

}
