package com.sakura.xdvideo.mapper;

import com.sakura.xdvideo.domain.Video;
import com.sakura.xdvideo.provider.VideoProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * video数据访问层
 *
 * @author sakura
 */
public interface VideoMapper {

    @Select("select * from video")
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE id = #{id}")
    Video findById(int id);

    //@Update("UPDATE video SET title=#{title} WHERE id =#{id}")
    @UpdateProvider(type = VideoProvider.class, method = "updateVideo")
    int update(Video video);

    @Delete("DELETE FROM video WHERE id =#{id}")
    int delete(int id);

    @Insert("INSERT INTO `video` ( `title`, `summary`, " +
            "`cover_img`, `view_num`, `price`, `create_time`," +
            " `online`, `point`)" +
            "VALUES" +
            "(#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price}, #{createTime}" +
            ",#{online}, #{point});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(Video video);
}
