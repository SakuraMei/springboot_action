package com.sakura.xdvideo.service.impl;

import com.sakura.xdvideo.domain.Video;
import com.sakura.xdvideo.mapper.VideoMapper;
import com.sakura.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sakura
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> findAll() {
        return videoMapper.findAll();
    }

    @Override
    public Video findById(int id) {
        return videoMapper.findById(id);
    }

    @Override
    public int update(Video video) {
        return videoMapper.update(video);
    }

    @Override
    public int delete(int id) {
        return videoMapper.delete(id);
    }

    @Override
    public int save(Video video) {
        int rows = videoMapper.save(video);
        System.out.println("保存对象的id= " + video.getId());
        return rows;
    }
}
