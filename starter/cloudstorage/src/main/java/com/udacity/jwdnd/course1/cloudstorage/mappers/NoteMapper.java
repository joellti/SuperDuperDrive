package com.udacity.jwdnd.course1.cloudstorage.mappers;

/*noteid INT PRIMARY KEY auto_increment,
notetitle VARCHAR(20),
notedescription VARCHAR (1000),
userid INT,
foreign key (userid) references USERS(userid)*/

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{notetitle}, #{notedescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    public int insert(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    public int update(Note note);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> findByUserId(Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    public Note findByNoteId(Integer noteId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    public int delete(Integer noteId);

}
