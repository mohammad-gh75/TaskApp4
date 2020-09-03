package org.maktab36.taskapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.maktab36.taskapp.util.DateUtils;

import java.util.Date;
import java.util.UUID;
@Entity(tableName = "TaskTable")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "uuid")
    private UUID mUUID;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "state")
    private TaskState mState;
    @ColumnInfo(name = "description")
    private String mDescription;
    @ColumnInfo(name = "date")
    private Date mDate;
    @ColumnInfo(name = "userId")
    private UUID mUserId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return mUserId;
    }

    public void setUserId(UUID userId) {
        mUserId = userId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public TaskState getState() {
        return mState;
    }

    public void setState(TaskState state) {
        mState = state;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Task() {

    }

    public Task(UUID userId) {
        this(UUID.randomUUID(),
                null,
                null,
                null,
                DateUtils.getRandomDate(2000,2020),
                userId);
    }


    public Task(UUID userId,String name, TaskState state) {
        this(UUID.randomUUID(),
                name,
                state,
                null,
                DateUtils.getRandomDate(2000,2020),
                userId);
    }

    public String getPhotoFileName() {
        return "IMG_" + getUUID() + ".jpg";
    }

    public Task(UUID UUID, String name, TaskState state, String description, Date date, UUID userId) {
        mUUID = UUID;
        mName = name;
        mState = state;
        mDescription = description;
        mDate = date;
        mUserId = userId;
    }
}
