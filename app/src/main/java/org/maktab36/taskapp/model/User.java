package org.maktab36.taskapp.model;

import java.util.Date;
import java.util.UUID;

public class User {
    private UUID mId;
    private String mUsername;
    private String mPassword;
    private Date mMembershipDate;


    public User() {
        this(UUID.randomUUID(), null, null, new Date());
    }

    public User(String username, String password) {
        this(UUID.randomUUID(), username, password, new Date());
    }

    public User(UUID id, String username, String password) {
        this(id, username, password, new Date());
    }

    public User(UUID id, String username, String password, Date membershipDate) {
        mId = id;
        mUsername = username;
        mPassword = password;
        mMembershipDate = membershipDate;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
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
