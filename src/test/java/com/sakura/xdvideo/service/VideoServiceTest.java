package com.sakura.xdvideo.service;

import com.sakura.xdvideo.domain.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Test
    public void findAll() {
        List<Video> videoList = videoService.findAll();
        assertNotNull(videoList);
    }

    @Test
    public void findById() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void save() {
    }
}