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

import hcmute.vika.myapplication.Model.MyItem;
import hcmute.vika.myapplication.Model.Song;
import hcmute.vika.myapplication.MusicPlayList;
import hcmute.vika.myapplication.MusicPlayerActivity;
import hcmute.vika.myapplication.R;
import hcmute.vika.myapplication.Service.MyService;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<MyItem> items=new ArrayList<>();

    public ListAdapter(Context context, List<MyItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imgList = view.findViewById(R.id.img_list);
            viewHolder.tvTitleAlbum = view.findViewById(R.id.tv_title_album);
            Picasso.get().load(items.get(position).getImage()).into(viewHolder.imgList);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        MyItem item = items.get(position);

//        viewHolder.imgList.setImageResource(R.drawable.album);
        viewHolder.tvTitleAlbum.setText(item.getAlbumTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum(position);
            }
        });
        return view;
    }


    static class ViewHolder {
        ImageView imgList;
        TextView tvTitleAlbum;
    }
    public void openAlbum(int position) {
        MyItem myItem = items.get(position);
        Intent intent = new Intent(context, MusicPlayList.class);
        intent.putExtra("key_music",myItem.getAlbumTitle());
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //intent.putExtras(bundle1);
        context.startActivity(intent);
    }
}
