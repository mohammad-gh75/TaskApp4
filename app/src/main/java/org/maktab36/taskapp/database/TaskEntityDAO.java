package org.maktab36.taskapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.maktab36.taskapp.model.Task;

import java.util.List;

@Dao
public interface TaskEntityDAO {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM TaskTable WHERE userId=:userId")
    void deleteAll(String userId);

    @Query("DELETE FROM TaskTable")
    void deleteAll();

    @Query("SELECT * FROM TaskTable WHERE state=:state")
    List<Task> getTasks(String state);

    @Query("SELECT * FROM TaskTable WHERE state=:state AND userId=:userId")
    List<Task> getTasks(String userId,String state);


    @Query("SELECT * FROM TaskTable WHERE uuid=:uuid")
    Task get(String uuid);

    @Query("SELECT * FROM TaskTable WHERE uuid=:uuid AND userId=:userId")
    Task get(String userId, String uuid);

    /*@Query("SELECT * FROM TaskTable WHERE name LIKE :name")
    List<Task> searchTasks(String name);

    @Query("SELECT * FROM TaskTable WHERE name LIKE :name AND description LIKE :description")
    List<Task> searchTasks(String name,String description);

    @Query("SELECT * FROM TaskTable WHERE name LIKE :name AND description LIKE :description" +
            " AND userId=:userId")
    List<Task> searchTasks(String name,String description,String userId);

    @Query("SELECT * FROM TaskTable WHERE name LIKE :name AND description LIKE :description" +
            " AND userId=:userId AND date >= :dateFrom")
    List<Task> searchTasks(String name,String description,String userId,String dateFrom);*/

    @Query("SELECT * FROM TaskTable WHERE name LIKE :name AND description LIKE :description" +
            " AND date >= :dateFrom AND date <= :dateTo")
    List<Task> searchTasks(String name,String description,long dateFrom,long dateTo);

    @Query("SELECT * FROM TaskTable WHERE name LIKE :name AND description LIKE :description" +
            " AND userId=:userId AND date >= :dateFrom AND date <= :dateTo")
    List<Task> searchTasks(String name,String description,long dateFrom,long dateTo,String userId);

    @Query("SELECT count(*) FROM TaskTable WHERE userId=:userId")
    int getNumberOfUserTasks(String userId);
}
