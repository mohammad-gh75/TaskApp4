package org.maktab36.taskapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;

@Entity(tableName = "UserTable")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "uuid")
    private UUID mUUID;
    @ColumnInfo(name = "username")
    private String mUsername;
    @ColumnInfo(name = "password")
    private String mPassword;
    @ColumnInfo(name = "memberShopDate")
    private Date mMembershipDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User() {
        this(UUID.randomUUID(), null, null, new Date());
    }

    public User(String username, String password) {
        this(UUID.randomUUID(), username, password, new Date());
    }

    public User(UUID UUID, String username, String password) {
        this(UUID, username, password, new Date());
    }

    public User(UUID UUID, String username, String password, Date membershipDate) {
        mUUID = UUID;
        mUsername = username;
        mPassword = password;
        mMembershipDate = membershipDate;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public Date getMembershipDate() {
        return mMembershipDate;
    }

    public void setMembershipDate(Date membershipDate) {
        mMembershipDate = membershipDate;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return mUsername.equals(user.mUsername) &&
                mPassword.equals(user.mPassword);
    }
}
