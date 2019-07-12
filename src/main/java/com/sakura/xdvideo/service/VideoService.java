package com.sakura.xdvideo.service;

import com.sakura.xdvideo.domain.Video;

import java.util.List;

/**
 * 视频业务类接口
 *
 * @author sakura
 */
public interface VideoService {

    List<Video> findAll();

    Video findById(int id);

    int update(Video video);

    int delete(int id);

    int save(Video video);
}
