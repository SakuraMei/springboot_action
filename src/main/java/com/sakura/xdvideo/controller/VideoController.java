package com.sakura.xdvideo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sakura.xdvideo.domain.Video;
import com.sakura.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * video接口
 *
 * @author sakura
 */
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * @param page 当前第几页，默认是第一页
     * @param size 每页显示几条
     * @return
     */
    @GetMapping("page")
    public Object pageVideo(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        List<Video> videoList = videoService.findAll();
        PageInfo<Video> pageInfo = new PageInfo<>(videoList);

        Map<String, Object> data = new HashMap<>();

        /**
         * 总条数
         */
        data.put("total_size", pageInfo.getTotal());

        /**
         * 总页数
         */
        data.put("total_page", pageInfo.getPages());

        /**
         * 当前页
         */
        data.put("current_page", page);

        /**
         * 数据
         */
        data.put("data", pageInfo.getList());

        return data;
    }

    /**
     * 根据Id查询视频
     *
     * @param videoId
     * @return
     */
    @GetMapping("find_by_id")
    public Object findById(@RequestParam(value = "video_id", required = true) int videoId) {
        return videoService.findById(videoId);
    }

}
