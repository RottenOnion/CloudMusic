package com.example.cloudmusic.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/12/20.
 */

public class SongData implements Parcelable {
    private String m4a ;
    private String media_mid ;
    private int songid;
    private int singerid;
    private String albumname ;
    private String downUrl ;
    private String singername ;
    private String songname ;
    private String strMediaMid ;
    private String albummid ;
    private String songmid ;
    private String albumpic_big ;
    private String albumpic_small ;
    private String albumid ;
    private String lrc_path;

    public SongData() {
    }

    public SongData(String m4a, String media_mid, int songid, int singerid, String albumname,
                    String downUrl, String singername, String songname, String strMediaMid,
                    String albummid, String songmid, String albumpic_big, String albumpic_small, String albumid,String lrc_path) {
        this.m4a = m4a;
        this.media_mid = media_mid;
        this.songid = songid;
        this.singerid = singerid;
        this.albumname = albumname;
        this.downUrl = downUrl;
        this.singername = singername;
        this.songname = songname;
        this.strMediaMid = strMediaMid;
        this.albummid = albummid;
        this.songmid = songmid;
        this.albumpic_big = albumpic_big;
        this.albumpic_small = albumpic_small;
        this.albumid = albumid;
        this.lrc_path = lrc_path;
    }

    public String getM4a() {
        return m4a;
    }

    public void setM4a(String m4a) {
        this.m4a = m4a;
    }

    public String getMedia_mid() {
        return media_mid;
    }

    public void setMedia_mid(String media_mid) {
        this.media_mid = media_mid;
    }

    public int getSongid() {
        return songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public int getSingerid() {
        return singerid;
    }

    public void setSingerid(int singerid) {
        this.singerid = singerid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getStrMediaMid() {
        return strMediaMid;
    }

    public void setStrMediaMid(String strMediaMid) {
        this.strMediaMid = strMediaMid;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getAlbumpic_big() {
        return albumpic_big;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    public String getAlbumpic_small() {
        return albumpic_small;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public void setLrc_path(String lrc_path) {
        this.lrc_path = lrc_path;
    }

    public String getLrc_path() {
        return  lrc_path;
    }

    @Override
    public String toString() {
        return "SongData{" +
                "m4a='" + m4a + '\'' +
                ", media_mid='" + media_mid + '\'' +
                ", songid=" + songid +
                ", singerid=" + singerid +
                ", albumname='" + albumname + '\'' +
                ", downUrl='" + downUrl + '\'' +
                ", singername='" + singername + '\'' +
                ", songname='" + songname + '\'' +
                ", strMediaMid='" + strMediaMid + '\'' +
                ", albummid='" + albummid + '\'' +
                ", songmid='" + songmid + '\'' +
                ", albumpic_big='" + albumpic_big + '\'' +
                ", albumpic_small='" + albumpic_small + '\'' +
                ", albumid='" + albumid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(m4a);
        parcel.writeString(media_mid);
        parcel.writeInt(songid);
        parcel.writeInt(singerid);
        parcel.writeString(albumname);
        parcel.writeString(downUrl);
        parcel.writeString(singername);
        parcel.writeString(songname);
        parcel.writeString(strMediaMid);
        parcel.writeString(albummid);
        parcel.writeString(songmid);
        parcel.writeString(albumpic_big);
        parcel.writeString(albumpic_small);
        parcel.writeString(albumid);
        parcel.writeString(lrc_path);
    }

    public static final Parcelable.Creator<SongData> CREATOR = new Creator<SongData>() {
        @Override
        public SongData createFromParcel(Parcel parcel) {
            return new SongData(parcel.readString(),parcel.readString(),parcel.readInt(),parcel.readInt(),
                    parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),
                    parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),parcel.readString(),
                    parcel.readString());
        }

        @Override
        public SongData[] newArray(int i) {
            return new SongData[i];
        }
    };
}
