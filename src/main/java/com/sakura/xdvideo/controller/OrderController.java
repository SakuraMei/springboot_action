package com.sakura.xdvideo.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sakura.xdvideo.dto.VideoOrderDto;
import com.sakura.xdvideo.service.VideoOrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * 订单接口
 *
 * @author sakura
 */
@RestController
//@RequestMapping("/user/api/v1/order")
@RequestMapping("/api/v1/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Logger dataLogger = LoggerFactory.getLogger("dataLogger");


    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * 下单接口
     *
     * @param videoId
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("add")
    public void saveOrder(@RequestParam(value = "video_id", required = true) int videoId,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String ip = IpUtils.getIpAddr(request);
//        Integer userId = (Integer) request.getAttribute("user_id");
        String ip = "120.1.25.43";
        int userId = 1;

        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setUserId(userId);
        videoOrderDto.setIp(ip);
        String codeUrl = videoOrderService.save(videoOrderDto);

        if (codeUrl == null) {
            throw new NullPointerException();
        }

        try {
            /**
             * 生成二维码
             */
            HashMap<EncodeHintType, Object> hints = new HashMap<>();
            /**
             * 设置纠错等级
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            /**
             * 设置编码类型
             */
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400, hints);
            OutputStream outputStream = response.getOutputStream();

            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
