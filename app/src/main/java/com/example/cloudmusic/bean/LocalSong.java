package com.example.cloudmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/12/8.
 */

public class LocalSong implements Parcelable{
    private String singer;
    private String song;
    private String path;
    private int duration;

    public LocalSong() {

    }

    public LocalSong(String singer,String song,String path,int duration) {
        this.singer = singer;
        this.song = song;
        this.path = path;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(singer);
        parcel.writeString(song);
        parcel.writeString(path);
        parcel.writeInt(duration);
    }

    public static final Parcelable.Creator<LocalSong> CREATOR = new Creator<LocalSong>() {
        @Override
        public LocalSong createFromParcel(Parcel parcel) {
            return new LocalSong(parcel.readString(),parcel.readString(),parcel.readString(),parcel.readInt());
        }

        @Override
        public LocalSong[] newArray(int i) {
            return new LocalSong[i];
        }
    };
}
