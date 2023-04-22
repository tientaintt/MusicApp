package hcmute.vika.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hcmute.vika.myapplication.MusicPlayerActivity;
import hcmute.vika.myapplication.R;
import hcmute.vika.myapplication.Model.Song;
import hcmute.vika.myapplication.Service.MyService;

public class ListSongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> arraylist = new ArrayList<>();


    public ListSongAdapter(Context context, int layout, List<Song> arraylist) {
        this.context = context;
        this.layout = layout;
        this.arraylist = arraylist;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView=inflater.inflate(layout, null);
//        Song song = arraylist.get(position);
        viewHolder holder;
        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView=inflater.inflate(layout, null);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);
            holder = new viewHolder();
            holder.imageSong = convertView.findViewById(R.id.img_song);
            Picasso.get().load(arraylist.get(position).getImage()).into(holder.imageSong);

            holder.nameSong = convertView.findViewById(R.id.tv_title_song1);
            holder.singer = convertView.findViewById(R.id.tv_name_artist);
            convertView.setTag(holder);


        } else {
            holder = (viewHolder) convertView.getTag();
        }

        Song song = arraylist.get(position);

        holder.nameSong.setText(song.getName());
//        Glide.with(context).load(song.getImage()).into(holder.imageSong);
        holder.singer.setText(song.getArtist());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSong(position);
            }
        });
        return convertView;
    }

    public class viewHolder {
        TextView singer, nameSong;
        ImageView imageSong;
    }

    public void openSong(int position) {
        Song song = arraylist.get(position);
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        //Bundle bundle1 = new Bundle();
        //bundle1.putSerializable("Song", song);
        intent.putExtra("positionSong",position);
        //intent.putExtras(bundle1);
        context.startActivity(intent);
        Intent intent1 = new Intent(context, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Song", song);


        intent1.putExtras(bundle);

        context.startService(intent1);
    }
    public void getimage(String path){

    }

}
