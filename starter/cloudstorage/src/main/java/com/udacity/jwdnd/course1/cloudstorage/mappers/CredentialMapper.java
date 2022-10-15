package com.udacity.jwdnd.course1.cloudstorage.mappers;

/*
CREATE TABLE IF NOT EXISTS CREDENTIALS (
credentialid INT PRIMARY KEY auto_increment,
url VARCHAR(100),
username VARCHAR (30),
key VARCHAR,
password VARCHAR,
userid INT,
foreign key (userid) references USERS(userid)
);
*/

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid ) VALUES(#{url}, #{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    public int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName}, key = #{key}, password = #{password}, userId = #{userId} WHERE credentialid = #{credentialid}")
    public int update(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> findByUserId(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    public Credential findByCredentialId(Integer credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    public int delete(Integer credentialId);
}
