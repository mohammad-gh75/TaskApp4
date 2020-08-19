package org.maktab36.taskapp.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.maktab36.taskapp.database.DataBaseHelper;
import org.maktab36.taskapp.database.TaskDBSchema;
import org.maktab36.taskapp.database.TaskDBSchema.UserTable;
import org.maktab36.taskapp.database.cursorwrapper.UserCursorWrapper;
import org.maktab36.taskapp.model.Task;
import org.maktab36.taskapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRepository {
    private static UserRepository sUserRepository;
    private static Context mContext;
    private SQLiteDatabase mDatabase;
    private User mCurrentUser;
    private User mAdmin;

    public static UserRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sUserRepository == null) {
            sUserRepository = new UserRepository();
        }
        return sUserRepository;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        UserCursorWrapper userCursorWrapper = queryUsers(null, null);
        try {
            userCursorWrapper.moveToFirst();
            while (!userCursorWrapper.isAfterLast()) {
                users.add(userCursorWrapper.getUser());
                userCursorWrapper.moveToNext();
            }
        } finally {
            userCursorWrapper.close();
        }
        return users;
    }

    private UserRepository() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
        mDatabase = dataBaseHelper.getWritableDatabase();
        if(getUser("admin","1234")==null){
            mAdmin = new User("admin", "1234");
            addUser(mAdmin);
        }else{
            mAdmin=getUser("admin","1234");
        }
    }

    public User getAdmin() {
        return mAdmin;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }


    public void addUser(User user) {
        if (!containUser(user)) {
            ContentValues values = getUserContentValue(user);
            mDatabase.insert(UserTable.NAME, null, values);
        }
    }

    public void deleteUser(User user) {
        String selection=UserTable.COLS.UUID + "=?";
        String[] selectionArgs= new String[]{user.getId().toString()};
        mDatabase.delete(UserTable.NAME, selection, selectionArgs);
    }

    public User getUser(String username, String password) {
        String selection = UserTable.COLS.USERNAME + "=? AND " + UserTable.COLS.PASSWORD + "=?";
        String[] selectionArgs = new String[]{username, password};
        UserCursorWrapper userCursorWrapper = queryUsers(selection, selectionArgs);
        try {
            userCursorWrapper.moveToFirst();
            if(userCursorWrapper.getCount()==0){
                return null;
            }else{
                return userCursorWrapper.getUser();
            }
        } finally {
            userCursorWrapper.close();
        }
    }

    public User getUser(UUID userId) {
        String selection = UserTable.COLS.UUID + "=?";
        String[] selectionArgs = new String[]{userId.toString()};
        UserCursorWrapper userCursorWrapper = queryUsers(selection, selectionArgs);
        try {
            userCursorWrapper.moveToFirst();
            return userCursorWrapper.getUser();
        } finally {
            userCursorWrapper.close();
        }
    }

    public boolean containUser(User user) {
        String selection = UserTable.COLS.USERNAME + "=? AND " + UserTable.COLS.PASSWORD + "=?";
        String[] selectionArgs = new String[]{user.getUsername(), user.getPassword()};
        UserCursorWrapper userCursorWrapper = queryUsers(selection, selectionArgs);
        try {
            userCursorWrapper.moveToFirst();
            return userCursorWrapper.getCount() != 0;
        } finally {
            userCursorWrapper.close();
        }
    }

    public boolean containUser(String username) {
        String selection = UserTable.COLS.USERNAME + "=?";
        String[] selectionArgs = new String[]{username};
        UserCursorWrapper userCursorWrapper = queryUsers(selection, selectionArgs);
        try {
            userCursorWrapper.moveToFirst();
            return userCursorWrapper.getCount() != 0;
        } finally {
            userCursorWrapper.close();
        }
    }

    private UserCursorWrapper queryUsers(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(UserTable.NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        return new UserCursorWrapper(cursor);
    }

    private ContentValues getUserContentValue(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.COLS.UUID, user.getId().toString());
        values.put(UserTable.COLS.USERNAME, user.getUsername());
        values.put(UserTable.COLS.PASSWORD, user.getPassword());
        values.put(UserTable.COLS.MEMBERSHIP_DATE, user.getMembershipDate().getTime());
        return values;
    }
}
