package hcmute.vika.myapplication.Model;

import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    private String name;
    private String artist;

    private String image;
    private String mp3;

    public Song(){

    }
    public Song(String name, String artist, String mp3, String image) {
        this.name = name;
        this.artist = artist;
        this.mp3 = mp3;
        this.image=image;
    }

}
