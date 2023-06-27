package hcmute.vika.myapplication.Model;

import android.content.Context;

import hcmute.vika.myapplication.PlayListActivity;

public class MyItem {
    private int image;
    private String albumTitle;

    public MyItem(int image, String albumTitle) {
        this.image = image;
        this.albumTitle = albumTitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    //return 0 if dont find image drawable/imageName
    private int getImageResource(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}