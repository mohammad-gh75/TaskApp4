package org.maktab36.taskapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.maktab36.taskapp.database.DataBaseHelper;
import org.maktab36.taskapp.database.TaskDBSchema.TaskTable;
import org.maktab36.taskapp.database.cursorwrapper.TaskCursorWrapper;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.TaskState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskRepository {
    private static TaskRepository sTaskRepository;
    private static Context mContext;
    private SQLiteDatabase mDatabase;
    private UUID mAdminId;

    public static TaskRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sTaskRepository == null) {
            sTaskRepository = new TaskRepository();
        }
        return sTaskRepository;
    }

    private TaskRepository() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
        mDatabase = dataBaseHelper.getWritableDatabase();
        mAdminId = UserRepository.getInstance(mContext).getAdmin().getId();
    }

    public List<Task> getToDoTasks(UUID userId) {
        List<Task> toDoTasks = new ArrayList<>();
        String selection;
        String[] selectionArgs;
        if (userId.equals(mAdminId)) {
            selection = TaskTable.COLS.STATE + "=?";
            selectionArgs = new String[]{TaskState.TODO.toString()};
        } else {
            selection = TaskTable.COLS.STATE + "=? AND " + TaskTable.COLS.USER_ID + "=?";
            selectionArgs = new String[]{TaskState.TODO.toString(), userId.toString()};
        }
        TaskCursorWrapper taskCursorWrapper = queryTasks(selection, selectionArgs);
        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                toDoTasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }
        } finally {
            taskCursorWrapper.close();
        }
        return toDoTasks;
    }

    public List<Task> getDoneTasks(UUID userId) {
        List<Task> doneTasks = new ArrayList<>();
        String selection;
        String[] selectionArgs;
        if (userId.equals(mAdminId)) {
            selection = TaskTable.COLS.STATE + "=?";
            selectionArgs = new String[]{TaskState.DONE.toString()};
        } else {
            selection = TaskTable.COLS.STATE + "=? AND " + TaskTable.COLS.USER_ID + "=?";
            selectionArgs = new String[]{TaskState.DONE.toString(), userId.toString()};
        }
        TaskCursorWrapper taskCursorWrapper = queryTasks(selection, selectionArgs);
        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                doneTasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }
        } finally {
            taskCursorWrapper.close();
        }
        return doneTasks;
    }


    public List<Task> getDoingTasks(UUID userId) {
        List<Task> doingTasks = new ArrayList<>();
        String selection;
        String[] selectionArgs;
        if (userId.equals(mAdminId)) {
            selection = TaskTable.COLS.STATE + "=?";
            selectionArgs = new String[]{TaskState.DOING.toString()};
        } else {
            selection = TaskTable.COLS.STATE + "=? AND " + TaskTable.COLS.USER_ID + "=?";
            selectionArgs = new String[]{TaskState.DOING.toString(), userId.toString()};
        }
        TaskCursorWrapper taskCursorWrapper = queryTasks(selection, selectionArgs);
        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                doingTasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }
        } finally {
            taskCursorWrapper.close();
        }
        return doingTasks;
    }

    public Task get(UUID userId, UUID uuid) {
        String selection;
        String[] selectionArgs;
        if (userId.equals(mAdminId)) {
            selection = TaskTable.COLS.UUID + "=?";
            selectionArgs = new String[]{uuid.toString()};
        } else {
            selection = TaskTable.COLS.UUID + "=? AND " + TaskTable.COLS.USER_ID + "=?";
            selectionArgs = new String[]{uuid.toString(), userId.toString()};
        }
        TaskCursorWrapper taskCursorWrapper = queryTasks(selection, selectionArgs);
        try {
            taskCursorWrapper.moveToFirst();
            return taskCursorWrapper.getTask();
        } finally {
            taskCursorWrapper.close();
        }
    }

    public void update(Task task) {
        ContentValues values = getTaskContentValue(task);
        String where = TaskTable.COLS.UUID + "=?";
        String[] whereArgs = new String[]{task.getId().toString()};
        mDatabase.update(TaskTable.NAME, values, where, whereArgs);
    }

    public void delete(Task task) {
        String selection;
        String[] selectionArgs;
        selection = TaskTable.COLS.UUID + "=?";
        selectionArgs = new String[]{task.getId().toString()};
        mDatabase.delete(TaskTable.NAME, selection, selectionArgs);
    }


    public void insert(Task task) {
        ContentValues values = getTaskContentValue(task);
        mDatabase.insert(TaskTable.NAME, null, values);
    }

    public void deleteAll(UUID userId) {
        String selection;
        String[] selectionArgs;
        if (userId.equals(mAdminId)) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = TaskTable.COLS.USER_ID + "=?";
            selectionArgs = new String[]{userId.toString()};
        }
        mDatabase.delete(TaskTable.NAME, selection, selectionArgs);
    }

    public List<Task> searchTasks(UUID userId, String name, String description, Date dateFrom, Date dateTo) {
        List<Task> searchedTasks = new ArrayList<>();
        StringBuilder selectBuilder = new StringBuilder();
        List<String> selectionList = new ArrayList<>();
        if (!name.equals("")) {
            if (selectBuilder.length() != 0) {
                selectBuilder.append(" AND ");
            }
            selectBuilder.append(TaskTable.COLS.NAME + " LIKE ?");
            selectionList.add("%"+name+"%");
        }
        if (!description.equals("")) {
            if (selectBuilder.length() != 0) {
                selectBuilder.append(" AND ");
            }
            selectBuilder.append(TaskTable.COLS.DESCRIPTION + " LIKE ?");
            selectionList.add("%"+description+"%");
        }
        if (dateFrom!=null) {
            if (selectBuilder.length() != 0) {
                selectBuilder.append(" AND ");
            }
            selectBuilder.append(TaskTable.COLS.DATE + ">=?");
            selectionList.add(String.valueOf(dateFrom.getTime()));
        }
        if (dateTo!=null) {
            if (selectBuilder.length() != 0) {
                selectBuilder.append(" AND ");
            }
            selectBuilder.append(TaskTable.COLS.DATE + "<=?");
            selectionList.add(String.valueOf(dateTo.getTime()));
        }
        if (!userId.equals(mAdminId)) {
            if (selectBuilder.length() != 0) {
                selectBuilder.append(" AND ");
            }
            selectBuilder.append(TaskTable.COLS.USER_ID + "=?");
            selectionList.add(userId.toString());
        }
        String selection = selectBuilder.toString();
        String[] selectionArgs=new String[selectionList.size()];
        selectionList.toArray(selectionArgs);
        TaskCursorWrapper taskCursorWrapper = queryTasks(selection, selectionArgs);
        try {
            taskCursorWrapper.moveToFirst();
            while (!taskCursorWrapper.isAfterLast()) {
                searchedTasks.add(taskCursorWrapper.getTask());
                taskCursorWrapper.moveToNext();
            }
        } finally {
            taskCursorWrapper.close();
        }
        return searchedTasks;
    }

    public int getNumberOfUserTasks(UUID userId) {
        String selection = TaskTable.COLS.USER_ID + "=?";
        String[] selectionArgs = new String[]{userId.toString()};
        TaskCursorWrapper taskCursorWrapper = queryTasks(selection, selectionArgs);
        try {
            taskCursorWrapper.moveToFirst();
            return taskCursorWrapper.getCount();
        } finally {
            taskCursorWrapper.close();
        }
    }

    private TaskCursorWrapper queryTasks(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(TaskTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        return new TaskCursorWrapper(cursor);
    }

    private ContentValues getTaskContentValue(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskTable.COLS.UUID, task.getId().toString());
        values.put(TaskTable.COLS.NAME, task.getName());
        values.put(TaskTable.COLS.DATE, task.getDate().getTime());
        values.put(TaskTable.COLS.STATE, task.getState().toString());
        values.put(TaskTable.COLS.DESCRIPTION, task.getDescription());
        values.put(TaskTable.COLS.USER_ID, task.getUserId().toString());
        return values;
    }
}
