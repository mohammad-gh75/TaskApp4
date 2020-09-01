package org.maktab36.taskapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface UserEntityDAO {
    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM UserTable")
    List<User> getUsers();

    @Query("SELECT * FROM UserTable WHERE username=:username AND password=:password")
    User getUser(String username, String password);

    @Query("SELECT * FROM UserTable WHERE uuid=:userId")
    User getUser(String userId);

    @Query("SELECT count(*) FROM UserTable WHERE uuid=:userId")
    int containUser(String userId);

    @Query("SELECT count(*) FROM UserTable WHERE username=:username")
    int containUsername(String username);
}
