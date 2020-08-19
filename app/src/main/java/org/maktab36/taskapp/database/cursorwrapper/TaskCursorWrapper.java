package org.maktab36.taskapp.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.maktab36.taskapp.database.TaskDBSchema;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.TaskState;

import java.util.Date;
import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String stringUUId = getString(getColumnIndex(TaskDBSchema.TaskTable.COLS.UUID));
        String name = getString(getColumnIndex(TaskDBSchema.TaskTable.COLS.NAME));
        Date date = new Date(getLong(getColumnIndex(TaskDBSchema.TaskTable.COLS.DATE)));
        TaskState state = TaskState.valueOf(getString(getColumnIndex(TaskDBSchema.TaskTable.COLS.STATE)));
        String description = getString(getColumnIndex(TaskDBSchema.TaskTable.COLS.DESCRIPTION));
        String stringUserId = getString(getColumnIndex(TaskDBSchema.TaskTable.COLS.USER_ID));

        Task task = new Task(UUID.fromString(stringUUId),
                name,
                state,
                description,
                date,
                UUID.fromString(stringUserId));
        return task;
    }
}
