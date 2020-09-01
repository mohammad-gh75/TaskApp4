package org.maktab36.taskapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.User;

@Database(entities = {Task.class, User.class},version = 1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskDataBase extends RoomDatabase {
    public abstract TaskEntityDAO taskDao();
    public abstract UserEntityDAO userDao();
}
