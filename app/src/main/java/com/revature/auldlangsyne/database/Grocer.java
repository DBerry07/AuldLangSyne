package com.revature.auldlangsyne.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.joda.time.LocalTime;

import java.time.format.DateTimeFormatter;

@Entity(tableName = "grocers")
public class Grocer implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private String name;

    @NonNull
    private String seniorStartTime;

    @NonNull
    private String seniorEndTime;

    @NonNull
    private float latitude;

    @NonNull
    private float longitude;

    @Ignore
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public Grocer() {
    }

    public Grocer(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        int i = 0;

        // Order must match writeToParcel() method
        //this.id = Integer.parseInt(data[0]);
        //i += 1;
        this.name = data[i];
        i += 1;
        this.seniorStartTime = data[i].toString();
        i += 1;
        this.seniorEndTime = data[i].toString();
        i += 1;
        this.latitude = Float.parseFloat(data[i]);
        i += 1;
        this.longitude = Float.parseFloat(data[i]);
        i += 1;
    }


    public Grocer(//int id,
                  String name,
                  LocalTime seniorStartTime,
                  LocalTime seniorEndTime,
                  String latitude,
                  String longitude) {
        //this.id = id;
        this.name = name;
        this.seniorStartTime = seniorStartTime.toString();
        this.seniorEndTime = seniorEndTime.toString();
        this.latitude = Float.parseFloat(latitude);
        this.longitude = Float.parseFloat(longitude);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeniorStartTime(String startTime) {
        this.seniorStartTime = startTime;
    }

    public void setSeniorEndTime(String endTime) {
        this.seniorEndTime = endTime;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSeniorStartTime() {
        return seniorStartTime;
    }

    public String getSeniorEndTime() {
        return seniorEndTime;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                Integer.toString(this.id),
                this.name,
                this.seniorStartTime,
                this.seniorEndTime,
                Float.toString(this.latitude),
                Float.toString(this.longitude)
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Grocer createFromParcel(Parcel in) {
            return new Grocer(in);
        }

        public Grocer[] newArray(int size) {
            return new Grocer[size];
        }
    };
}
