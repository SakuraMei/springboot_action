package com.sakura.xdvideo.controller.admin;

import com.sakura.xdvideo.domain.Video;
import com.sakura.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * admin才能操作的接口
 *
 * @author sakura
 */
@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    /**
     * 根据Id删除视频
     *
     * @param videoId
     * @return
     */
    @DeleteMapping("del_by_id")
    public Object delById(@RequestParam(value = "video_id", required = true) int videoId) {
        return videoService.delete(videoId);
    }

    /**
     * 根据Id更新视频
     *
     * @param video
     * @return
     */
    @PutMapping("update_by_id")
    public Object update(@RequestBody Video video) {
        return videoService.update(video);
    }

    /**
     * 添加视频
     *
     * @param video
     * @return
     */
    @PostMapping("save")
    public Object save(@RequestBody Video video) {
        return videoService.save(video);
    }
}
