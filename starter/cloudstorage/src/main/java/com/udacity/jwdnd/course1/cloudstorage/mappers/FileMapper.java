package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
fileId INT PRIMARY KEY auto_increment,
filename VARCHAR,
contenttype VARCHAR,
filesize VARCHAR,
userid INT,
filedata BLOB,
*/

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFilesByUserId(Integer userid);

    @Select("SELECT * FROM FILES WHERE userid = #{userid} AND filename = #{filename}")
    File getFilesByUserIdFileName(Integer userid, String filename);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFilesByFileId(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES where fileId = #{fileId}")
    int deleteByFileId(Integer fileId);

}
