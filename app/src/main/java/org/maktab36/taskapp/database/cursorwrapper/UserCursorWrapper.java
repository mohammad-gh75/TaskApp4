package org.maktab36.taskapp.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.maktab36.taskapp.database.TaskDBSchema.UserTable;
import org.maktab36.taskapp.model.User;

import java.util.Date;
import java.util.UUID;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String stringUUId = getString(getColumnIndex(UserTable.COLS.UUID));
        String username = getString(getColumnIndex(UserTable.COLS.USERNAME));
        String password = getString(getColumnIndex(UserTable.COLS.PASSWORD));
        Date membershipDate=new Date(getLong(getColumnIndex(UserTable.COLS.MEMBERSHIP_DATE)));


        User user = new User(UUID.fromString(stringUUId),
                username,
                password,membershipDate);
        return user;
    }
}
