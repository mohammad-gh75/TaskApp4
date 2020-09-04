package org.maktab36.taskapp.repository;

import android.content.Context;

import androidx.room.Room;

import org.maktab36.taskapp.database.TaskDataBase;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.TaskState;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskRepository {
    private static TaskRepository sTaskRepository;
    private static Context mContext;
    private TaskDataBase mDatabase;
    private UUID mAdminId;

    public static TaskRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sTaskRepository == null) {
            sTaskRepository = new TaskRepository();
        }
        return sTaskRepository;
    }

    private TaskRepository() {
        mDatabase = Room.databaseBuilder(mContext,
                TaskDataBase.class,
                "TaskManagerDB")
                .allowMainThreadQueries()
                .build();
        mAdminId = UserRepository.getInstance(mContext).getAdmin().getUUID();
    }

    public List<Task> getToDoTasks(UUID userId) {
        if (userId.equals(mAdminId)) {
            return mDatabase.taskDao().getTasks(TaskState.TODO.toString());
        } else {
            return mDatabase.taskDao().getTasks(userId.toString(), TaskState.TODO.toString());
        }
    }

    public List<Task> getDoneTasks(UUID userId) {
        if (userId.equals(mAdminId)) {
            return mDatabase.taskDao().getTasks(TaskState.DONE.toString());
        } else {
            return mDatabase.taskDao().getTasks(userId.toString(), TaskState.DONE.toString());
        }
    }


    public List<Task> getDoingTasks(UUID userId) {
        if (userId.equals(mAdminId)) {
            return mDatabase.taskDao().getTasks(TaskState.DOING.toString());
        } else {
            return mDatabase.taskDao().getTasks(userId.toString(), TaskState.DOING.toString());
        }
    }

    public Task get(UUID userId, UUID uuid) {
        if (userId.equals(mAdminId)) {
            return mDatabase.taskDao().get(uuid.toString());
        } else {
            return mDatabase.taskDao().get(userId.toString(), uuid.toString());
        }
    }

    public void update(Task task) {
        mDatabase.taskDao().update(task);
    }

    public void delete(Task task) {
        mDatabase.taskDao().delete(task);
    }


    public void insert(Task task) {
        mDatabase.taskDao().insert(task);
    }

    public void deleteAll(UUID userId) {
        if (userId.equals(mAdminId)) {
            mDatabase.taskDao().deleteAll();
        } else {
            mDatabase.taskDao().deleteAll(userId.toString());
        }
    }
    public List<Task> searchTasks(UUID userId, String name, String description, Date dateFrom, Date dateTo) {
        String nameWhere = !name.equals("") ? ("%" + name + "%") : "%%";
        String descriptionWhere = !description.equals("") ? ("%" + description + "%") : "%%";
        long dateFromWhere = dateFrom != null ? dateFrom.getTime() : 0;
        long dateToWhere=dateTo !=null ? dateTo.getTime() : Long.MAX_VALUE;
        return !userId.equals(mAdminId) ?
                mDatabase.taskDao().searchTasks(
                nameWhere,
                descriptionWhere,
                dateFromWhere,
                dateToWhere,
                userId.toString()) :
                mDatabase.taskDao().searchTasks(
                        nameWhere,
                        descriptionWhere,
                        dateFromWhere,
                        dateToWhere );
    }

    public int getNumberOfUserTasks(UUID userId) {
        return mDatabase.taskDao().getNumberOfUserTasks(userId.toString());
    }

    public File getPhotoFile(Context context, Task task) {
        return new File(context.getFilesDir(), task.getPhotoFileName());
    }
}
