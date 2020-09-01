package org.maktab36.taskapp.database;

import androidx.room.TypeConverter;

import org.maktab36.taskapp.model.TaskState;

import java.util.Date;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String UUIDToString(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    @TypeConverter
    public static UUID fromStringUUID(String stringUUID) {
        return stringUUID == null ? null : UUID.fromString(stringUUID);
    }

    @TypeConverter
    public static String TaskStateToString(TaskState state){
        return state.toString();
    }

    @TypeConverter
    public static TaskState fromStringState(String state){
        return TaskState.valueOf(state);
    }
}
