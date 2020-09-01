package org.maktab36.taskapp.repository;

import android.content.Context;

import androidx.room.Room;

import org.maktab36.taskapp.database.TaskDataBase;
import org.maktab36.taskapp.model.User;

import java.util.List;
import java.util.UUID;

public class UserRepository {
    private static UserRepository sUserRepository;
    private static Context mContext;
    private TaskDataBase mDatabase;
    private User mCurrentUser;
    private User mAdmin;

    public static UserRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sUserRepository == null) {
            sUserRepository = new UserRepository();
        }
        return sUserRepository;
    }

    private UserRepository() {
        mDatabase = Room.databaseBuilder(mContext,
                TaskDataBase.class,
                "TaskManagerDB")
                .allowMainThreadQueries()
                .build();
        if (getUser("admin", "1234") == null) {
            mAdmin = new User("admin", "1234");
            addUser(mAdmin);
        } else {
            mAdmin = getUser("admin", "1234");
        }
    }

    public List<User> getUsers() {
        return mDatabase.userDao().getUsers();
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
            mDatabase.userDao().insert(user);
        }
    }

    public void deleteUser(User user) {
        mDatabase.userDao().delete(user);
    }

    public User getUser(String username, String password) {
        User user = mDatabase.userDao().getUser(username, password);
        return user;
    }

    public User getUser(UUID userId) {
        return mDatabase.userDao().getUser(userId.toString());
    }

    public boolean containUser(User user) {
        return mDatabase.userDao().containUser(user.getUUID().toString()) != 0;
    }

    public boolean containUser(String username) {
        return mDatabase.userDao().containUsername(username) != 0;
    }
}
