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

public class SearchMoreAdapter extends BaseAdapter {
    private Context mContext;
    private List<MusicModel> mList;

    public void setData(Context context, List<MusicModel> lists){
        this.mContext = context;
        this.mList = lists;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_search_more_musiclist,null,false);
            viewHolder.titleText = view.findViewById(R.id.tv_search_title);
            viewHolder.singerText = view.findViewById(R.id.tv_search_artist);
            convertView = view;
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MusicModel model = mList.get(position);
        if (model!=null){
            if (!model.getmTitle().equals("")){
                viewHolder.titleText.setText(model.getmTitle());
            }
            if (!model.getmArtist().equals("")){
                viewHolder.singerText.setText(model.getmArtist());
            }
        }
        return convertView;
    }

    class ViewHolder{
        TextView titleText,singerText;
    }
}
