package com.example.tangdan.cloudmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tangdan.cloudmusic.R;
import com.example.tangdan.cloudmusic.model.MusicModel;

import java.util.List;

public class LocalMicAdapter extends BaseAdapter {
    Context mContext;
    List<MusicModel> mList;

    public LocalMicAdapter(Context context, List<MusicModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MusicModel model = mList.get(position);

        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_localmusic_musiclist, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mArtist = view.findViewById(R.id.tv_localmusic_artist);
            viewHolder.mTitle = view.findViewById(R.id.tv_localmusic_title);
            view.setTag(viewHolder);
            return view;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (model != null) {
            viewHolder.mTitle.setText(model.getmTitle());
            viewHolder.mArtist.setText(model.getmArtist());
        }

        return convertView;
    }

    class ViewHolder {
        TextView mTitle;
        TextView mArtist;
    }
}
