package com.sakura.xdvideo.domain;


import java.io.Serializable;

/**
 * 章实体类
 *
 * @author sakura
 */
public class Chapter implements Serializable {

    private static final long serialVersionUID = 6541874419464257875L;
    private Integer id;
    private Integer videoId;
    private String title;
    private Integer ordered;
    private java.sql.Timestamp createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Integer getOrdered() {
        return ordered;
    }

    public void setOrdered(Integer ordered) {
        this.ordered = ordered;
    }


    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

}
